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

<h1>Generate Audit Log</h1>

<form role="form" name="form" action="" method="post">
  	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <div>
        <label for="userEmail">User: </label>
       
    	<select name="userEmail" id="userEmail">
    	<option value="">All selected</option>
    	 <#list users as user>   	 	
        	<option value="${user.email}">${user.email}</option>
         </#list>
    	</select>
    	<br>
    	Enter a start date
  <input type="date" name="startDate" id="startDate" min="2016-09-07" required><br>
  Enter end date
  <input type="date" name="endDate" id="endDate" required><br>
   		
    </div>
     <button type="submit">Generate Audit Log</button>
</form>

</body>
</html>