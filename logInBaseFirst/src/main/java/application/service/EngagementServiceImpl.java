package application.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import application.entity.Discount;
import application.entity.Event;
import application.entity.Feedback;
import application.entity.User;
import application.exception.EventNotFoundException;
import application.repository.AuditLogRepository;
import application.repository.CategoryRepository;
import application.repository.ClientOrganisationRepository;
import application.repository.DiscountRepository;
import application.repository.EventRepository;
import application.repository.FeedbackRepository;
import application.repository.TicketRepository;
import application.repository.UserRepository;

@Service
public class EngagementServiceImpl implements EngagementService{

	private final FeedbackRepository feedbackRepository;
	private final UserRepository userRepository;
	private final EventService eventService;
	private final AuditLogRepository auditLogRepository;
	private final EventRepository eventRepository;
	private final CategoryRepository categoryRepository;
	private final TicketRepository ticketRepository;
	private final DiscountRepository discountRepository;
	private final EmailService emailService;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	public EngagementServiceImpl(EmailService emailService, DiscountRepository discountRepository, 
			FeedbackRepository feedbackRepository, TicketRepository ticketRepository, CategoryRepository categoryRepository,
			EventRepository eventRepository, EventService eventService,AuditLogRepository auditLogRepository, 
			UserRepository userRepository) {
		super();
		this.auditLogRepository = auditLogRepository;
		this.emailService = emailService;
		this.userRepository = userRepository;
		this.eventService = eventService;
		this.eventRepository = eventRepository;
		this.categoryRepository = categoryRepository;
		this.ticketRepository = ticketRepository;
		this.feedbackRepository = feedbackRepository;
		this.discountRepository = discountRepository;
	}

	public void setFeedback(User usr, String cat, String msg){
		Feedback f = new Feedback();
		f.setCategory(cat);
		f.setFeedbackDate(new Date());
		f.setMessage(msg);
		f.setUser(usr);
		feedbackRepository.save(f);
	}

	public Discount getDiscount(String code){
		Discount d = discountRepository.getDiscountByCode(code);
		return d;

	}

	@Override
	public boolean addDiscount(String email, Long eventId, String retailerName, String message) throws EventNotFoundException {
		Event e = eventService.getEventById(eventId).get();
		try{
			Discount d = new Discount();

			SecureRandom random = new SecureRandom();
			String toUuid = new BigInteger(130,random).toString(32);
			String uuid =  toUuid.substring(0,9);	
			d.setQRCode(uuid);
			d.setDiscountMessage(message);
			d.setRetailerName(retailerName);
			e.addDiscount(d);
			eventRepository.save(e);
			//discountRepository.save(d);


			String myCodeText = uuid;
			String filePath = servletContext.getRealPath("/") + "/DiscountQRCode.png";
			System.err.println("FILE PATH IS " + filePath);
			int size = 250;
			String fileType = "png";
			File myFile = new File(filePath);
			try {

				Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
				hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

				// Now with zxing version 3.2.1 you could change border size (white border size to just 1)
				hintMap.put(EncodeHintType.MARGIN, 1); /* default = 4 */
				hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

				QRCodeWriter qrCodeWriter = new QRCodeWriter();
				BitMatrix byteMatrix = qrCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size,
						size, hintMap);
				int CrunchifyWidth = byteMatrix.getWidth();
				BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth,
						BufferedImage.TYPE_INT_RGB);
				image.createGraphics();

				Graphics2D graphics = (Graphics2D) image.getGraphics();
				graphics.setColor(Color.WHITE);
				graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
				graphics.setColor(Color.BLACK);

				for (int i = 0; i < CrunchifyWidth; i++) {
					for (int j = 0; j < CrunchifyWidth; j++) {
						if (byteMatrix.get(i, j)) {
							graphics.fillRect(i, j, 1, 1);
						}
					}
				}
				ImageIO.write(image, fileType, myFile);
				
				emailService.sendEmailWithAttachment(email, "Discount QR Code for " + retailerName, "Hello, attached is an image of the QR code for the discount you have just entered into the system. You may print this image out for further use.", filePath);


			} catch (WriterException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			System.out.println("\n\nYou have successfully created QR Code.");


			return true;
		}
		catch ( Exception ex){
			System.err.println("Exception at addDiscount in service class" + ex.getMessage());
		}

		return false;
	}

	@Override
	public Set<Discount> getDiscounts(Long eventId) throws EventNotFoundException {
		// TODO Auto-generated method stub
		Event e = eventService.getEventById(eventId).get();
		try{
			return e.getDiscounts();
		}
		catch ( Exception ex){
			System.err.println("Exception at getDiscounts in service class" + ex.getMessage());

		}
		return null;
	}

	@Override
	public boolean deleteDiscount(User user,Long discountId) {
		try{
			System.err.println("START TO DELETE");
			Discount discount = discountRepository.findOne(discountId);
			Set<Event> events = user.getEvents();
			for(Event ev : events){
				Set<Discount> dis = ev.getDiscounts();
				for(Discount d : dis){
					if(d.getId() == discountId){
						dis.remove(d);
						break;
					}
				}
				ev.setDiscounts(dis);
				eventRepository.flush();
			}
			System.err.println(discount.getDiscountMessage());
			discountRepository.delete(discount);		
		}
		catch ( Exception ex){
			System.err.println("FAIL TO DELETE");
			System.err.println("Error at deleting discount in service class" + ex.getMessage());
			return false;
		}		
		return true;
	}
	
	@Override
	public boolean updateDiscount(Long discountId,String name, String msg) {
		try{
			Discount d = discountRepository.findOne(discountId);
			d.setDiscountMessage(msg);
			d.setRetailerName(name);
			discountRepository.save(d);
			return true;
		}
		catch ( Exception ex){
			System.err.println("Error at updating discount in service class" + ex.getMessage());
		}
		return false;
	}

}
