package application.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import application.domain.ClientOrganisation;
import application.domain.PasswordResetToken;
import application.domain.User;
import application.repository.ClientOrganisationRepository;
import application.repository.PasswordResetTokenRepository;
import application.service.user.EmailService;
import application.service.user.UserService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

@Controller
public class LoginController {

	@Autowired
	private ApplicationContext context;
	private final UserService userService;
	private final EmailService emailService;
	private final PasswordResetTokenRepository passwordResetTokenRepository;
	private final ClientOrganisationRepository clientOrganisationRepository;

	private JSONParser parser = new JSONParser();

	@Autowired
	public LoginController(ClientOrganisationRepository clientOrganisationRepository, UserService userService,EmailService emailService, PasswordResetTokenRepository passwordResetTokenRepository) {
		super();
		this.userService = userService;
		this.emailService = emailService;
		this.passwordResetTokenRepository = passwordResetTokenRepository;
		this.clientOrganisationRepository = clientOrganisationRepository;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);


	@RequestMapping(value = "/user/loginVerify" , method = RequestMethod.GET)
	@ResponseBody
	public Principal user(Principal user) {
		if ( user != null) {
			System.out.println(user.getName()  + " - " + user.toString());
		}
		return user;
	}




	@RequestMapping(value="/logout", method = RequestMethod.POST)
	@ResponseBody
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){    
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "";//Maybe redirection
	}

	/*@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public ModelAndView getResetPasswordPage() {
		LOGGER.debug("Getting reset password form");
		return new ModelAndView("resetPassword", "form", new ResetPasswordForm());
	}
	 */

	/*	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public String getResetPasswordPage() {
		return "forward:resetPass.html";
	}*/

	/*@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public String handleResetPassword(@Valid @ModelAttribute("form") ResetPasswordForm form,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			// failed validation
			return "home"; //Need a dynamic error page
		}
		Optional<User> user = userService.getUserByEmail(form.getEmail());
		if (!user.isPresent() ) {
			return "home"; //Need a dynamic error page
		}
		User userToReset = user.get();
		Long userId = user.get().getId();
		String token = UUID.randomUUID().toString();
		String link = "The following link will expire in 30 minutes: http://localhost:8080/resetPassword?id=" + userId + "&token=" + token;
		userService.createPasswordResetTokenForUser(userToReset, token);
		emailService.sendEmail(form.getEmail(), "Reset your password by clicking on the link", link);
		return "home";
	}*/

	@RequestMapping(value = "/user/reset", method = RequestMethod.POST)
	public ResponseEntity<Void> resetPassword(@RequestBody String userEmail) throws URISyntaxException {
		System.out.println("Received Reset Password Request from  " + userEmail);

		if (!userService.getUserByEmail(userEmail).isPresent() ) {
			System.out.println("A User with that email does not exist");
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		System.out.println("Resetting password...");
		User userToReset = userService.getUserByEmail(userEmail).get();
		Long userId = userToReset.getId();
		String token = UUID.randomUUID().toString();
		String link = "The following link will expire in 30 minutes: https://localhost:8443/#/resetPassword/" + userId + "/" +  token;
		userService.createPasswordResetTokenForUser(userToReset, token);
		emailService.sendEmail(userEmail, "Reset your password by clicking on the link", link);
		System.out.println("Reset email has been sent to " + userEmail);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(new URI("/login"));
		ResponseEntity<Void> v = new ResponseEntity<Void>(headers,HttpStatus.OK);	
		return v;
	}


	@RequestMapping(value = "/user/resetChangePassword", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	@ResponseBody
	public ResponseEntity<Void> resetChangePassword(@RequestBody String info) throws URISyntaxException, IOException, ParseException {
		//	JSONParser parser = new JSONParser();
		Object obj = parser.parse(info);
		JSONObject jsonObject = (JSONObject) obj;
		User user = null;

		System.out.println("Received Reset Password Request from  " + jsonObject.get("id"));

		PasswordResetToken passToken = userService.getPasswordResetToken((String)jsonObject.get("token"));
		if ( passToken != null){
			user = passToken.getUser();
		}
		else{
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}


		System.out.println(user.getId());
		System.out.println((long)(jsonObject.get("id")));

		if (passToken == null || user.getId() != (long)(jsonObject.get("id"))) {
			//  String message = messages.getMessage("auth.message.invalidToken", null, locale);
			//  model.addAttribute("message", message);

			System.out.println("LINE 163");
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		Calendar cal = Calendar.getInstance();
		if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			//model.addAttribute("message", messages.getMessage("auth.message.expired", null, locale));
			System.out.println("LINE 169");
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);

		}

		System.out.println("Before password change");
		userService.changePassword(user.getId(), (String)(jsonObject.get("password")));
		passwordResetTokenRepository.delete(passToken);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(new URI("/login"));
		ResponseEntity<Void> v = new ResponseEntity<Void>(headers,HttpStatus.OK);	
		return v;
	}




	/*
	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public ModelAndView showChangePasswordPage(
			@RequestParam("id") long id, @RequestParam("token") String token) {

		PasswordResetToken passToken = userService.getPasswordResetToken(token);
		User user = passToken.getUser();
		if (passToken == null || user.getId() != id) {
			//  String message = messages.getMessage("auth.message.invalidToken", null, locale);
			//  model.addAttribute("message", message);
			return  new ModelAndView("login");
		}
		Calendar cal = Calendar.getInstance();
		if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			//model.addAttribute("message", messages.getMessage("auth.message.expired", null, locale));
			return  new ModelAndView("login");

		}
		return new ModelAndView("changePassword", "form", new ChangePasswordForm());

	}






	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public String handleChangePassword(@RequestParam("id") long id, @RequestParam("token") String token,@Valid @ModelAttribute("form") ChangePasswordForm form,BindingResult bindingResult) {
		System.out.println("Login Control Change Password Method");
		if (bindingResult.hasErrors()) {
			// failed validation
			return "home"; //Need a dynamic error page
		}
		Optional<User> user = userService.getUserById(id);
		if (!user.isPresent() ) {
			return "home"; //Need a dynamic error page
		}
		Long userId = user.get().getId();
		if ( !form.getPassword1().equals(form.getPassword2()) ){
			System.out.println("Non-matching password");
			System.out.println(form.getPassword1() + "    " + form.getPassword2());
			return "home";
		}
		else{
			System.out.println("Before password change");
			userService.changePassword(id, form.getPassword1());
		}


		return "home";
	}*/

	//------------------AUDIT LOG PORTION -------------------------------

	/*@RequestMapping("/viewAuditLogPage")
	public ModelAndView getViewMessagesPage() {
		LOGGER.debug("Getting view audit log page");
		return new ModelAndView("auditLog", "users", userService.getAllUsers());
	}
	 */
	/*	@RequestMapping(value = "/viewAuditLogPage", method = RequestMethod.POST)
	public void generateAuditReport(@Valid @ModelAttribute("form") AuditLogForm auditLogForm,BindingResult bindingResult, HttpServletRequest request,HttpServletResponse response) throws JRException, IOException {

		if (bindingResult.hasErrors()) {
			// failed validation
			System.err.println("ERROR AT GENERATING AUDIT REPORT");
		}


		InputStream jasperStream = request.getSession().getServletContext().getResourceAsStream("/jasper/AuditLog.jasper");
		response.setContentType("application/pdf");
		//FORCES AUTOMATIC DOWNLOAD
		response.setHeader("Content-disposition", "attachment; filename=AuditLog.pdf");
		ServletOutputStream outputStream = response.getOutputStream();
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		StringBuilder sb = new StringBuilder();
		sb.append("WHERE (time >= '");
		sb.append( auditLogForm.getStartDate() + " 00:00:00");
		sb.append("' AND ");
		sb.append("time <= '");
		sb.append(auditLogForm.getEndDate() + " 23:59:59");
		sb.append( "' )");
		if ( !auditLogForm.getUserEmail().isEmpty() || !auditLogForm.getUserEmail().equals("") ){
			sb.append("AND user_email = '" + auditLogForm.getUserEmail()  + "'");
		}
		parameters.put("criteria", sb.toString());
		Connection conn = null;
		try {
			DataSource ds = (DataSource)context.getBean("dataSource");
			conn = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JasperRunManager.runReportToPdfStream(jasperStream, outputStream, parameters,conn);
		outputStream.flush();
		outputStream.close();
	}*/


	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/receiveLogoFile")
	public void upload(@RequestParam("file") MultipartFile file, HttpServletRequest request ) throws IOException {

		Principal p = request.getUserPrincipal();
		User curUser = userService.getUserByEmail(p.getName()).get();

		if (!file.isEmpty()) {
			//GET RELATIVE PATH SINCE EVERYONE COMPUTER DIFFERENT
			String filePath = request.getSession().getServletContext().getRealPath("/");
			//filePath = filePath.replaceAll("webapp", "resources" + "\\\\" + "images");
			System.err.println(filePath);
			File toTrans = new File(filePath,file.getOriginalFilename());
			toTrans.setExecutable(true);
			toTrans.setWritable(true);
			file.transferTo(toTrans);
			//file.transferTo(new File(filePath));
			ClientOrganisation client = curUser.getClientOrganisation();
			client.setLogoFilePath(file.getOriginalFilename());
			clientOrganisationRepository.saveAndFlush(client);
			System.out.println("Saved Logo");

		}


		System.err.println(String.format("received %s", file.getOriginalFilename()));
	}


	@RequestMapping(value = "/getCompanyLogo", method = RequestMethod.GET)
	@ResponseBody
	public String getCompanyLogo(HttpServletRequest request ) throws IOException {
		try{
			Principal p = request.getUserPrincipal();
			User curUser = userService.getUserByEmail(p.getName()).get();
			System.err.println("getting logo for " + curUser.getEmail() + "w org " + curUser.getClientOrganisation().getOrganisationName());
			String temp = curUser.getClientOrganisation().getLogoFilePath();

			System.err.println("returning : " + temp);
			Gson gson = new Gson();
		    String json = gson.toJson(temp);
		    System.out.println(json);
		    return json;	
		}
		catch(Exception e){
			System.err.println(e.getMessage());
			return "ERROR";
		}
		//  return curUser.getClientOrganisation().getLogoFilePath();

	}


	@RequestMapping(value = "/downloadAuditLog", method = RequestMethod.POST, produces = "application/pdf")
	public void generateAuditReport(@RequestBody String info,HttpServletRequest request,HttpServletResponse response) throws JRException, IOException {
		System.out.println("Enter");
		InputStream jasperStream = request.getSession().getServletContext().getResourceAsStream("/jasper/AuditLog.jasper");
		response.setContentType("application/pdf");
		Principal principal = request.getUserPrincipal();
		//FORCES AUTOMATIC DOWNLOAD
		response.setHeader("Content-disposition", "attachment; filename=AuditLog.pdf");
		ServletOutputStream outputStream = response.getOutputStream();
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(" ");
		Object obj;
		String startDate = new String();
		String endDate = new String();
		String username = new String();
		try {
			obj = parser.parse(info);
			JSONObject jsonObject = (JSONObject) obj;
			//Returns null if unable to get the string, maybe because the name is wrong or it is missing in the json.
			username = (String)jsonObject.get("username");

			startDate = (String)jsonObject.get("startDate");
			endDate = (String)jsonObject.get("endDate");

			sb.append("WHERE (time >= '");
			sb.append( startDate + " 00:00:00");
			sb.append("' AND ");
			sb.append("time <= '");
			sb.append(endDate + " 23:59:59");
			sb.append( "' )");
			if ( username != null &&  userService.getUserByEmail(username).isPresent() ){
				sb.append("AND user = '" +  userService.getUserByEmail(username).get().getId()  + "'");
			}
			else{
				sb.append("AND( ");
				User curUser = userService.getUserByEmail(principal.getName()).get();
				for ( User us : curUser.getClientOrganisation().getUsers() ){
					sb.append("user =" +  us.getId() );
					sb.append(" OR ");
				}
				
				sb.setLength(sb.length() - 4);
				sb.append(")");
			}
			if ( (String)jsonObject.get("system") != null && (String)jsonObject.get("system") != "" ){
				sb.append(" AND system = '" + (String)jsonObject.get("system") + "'");
			}
			System.err.println("Query parameter is : " + sb.toString());
			parameters.put("criteria", sb.toString());
			Connection conn = null;
			try {
				DataSource ds = (DataSource)context.getBean("dataSource");
				conn = ds.getConnection();
				System.out.println(conn.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			JasperRunManager.runReportToPdfStream(jasperStream, outputStream, parameters,conn);
			outputStream.flush();
			outputStream.close();
			System.out.println("FLUSHED OUT THE LOG");
		} catch (ParseException e1) {
			System.out.println("at /download audit log there was an error parsing the json string received");
			e1.printStackTrace();
		}


	}
}

