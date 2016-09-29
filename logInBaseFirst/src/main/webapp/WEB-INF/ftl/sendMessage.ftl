<#-- @ftlvariable name="_csrf" type="org.springframework.security.web.csrf.CsrfToken" -->
<#-- @ftlvariable name="form" type="eu.kielczewski.example.domain.UserCreateForm" -->
<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Send a message</title>
</head>
<body>
<nav role="navigation">
    <ul>
        <li><a href="/">Home</a></li>
    </ul>
</nav>

<h1>Create a new message</h1>

<form role="form" name="form" action="" method="post">
  	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <div>
        <label for="Recipient">To: </label>
       
    	<select name="recipient" id="recipient">
    	 <#list users as user>
        	<option value="${user.email}">${user.email}</option>
         </#list>
    	</select>
   		
    </div>
    <div>
        <input type="text" name="message" id="message" required/>
    </div>
     <button type="submit">Send Message</button>
</form>

</body>
</html>