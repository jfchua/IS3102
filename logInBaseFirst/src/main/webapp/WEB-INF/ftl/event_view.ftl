<#-- @ftlvariable name="_csrf" type="org.springframework.security.web.csrf.CsrfToken" -->
<#-- @ftlvariable name="currentUser" type="eu.kielczewski.example.domain.CurrentUser" -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Event View</title>
</head>
<body>
	<h1> View Events </h1>
	  <tbody>
    <#list messages as message>
    <tr>
    	<td>${event.event_title} ${event.} ${event.event_content} ${event.event_description}</td>
        
    </tr>
    </#list>
    </tbody>
	

</form>

</body>
<html>