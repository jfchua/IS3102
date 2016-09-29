<#-- @ftlvariable name="_csrf" type="org.springframework.security.web.csrf.CsrfToken" -->
<#-- @ftlvariable name="currentUser" type="eu.kielczewski.example.domain.CurrentUser" -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Event Manage</title>
</head>
<body>
	<h1> Event Management </h1>
	
	<form role="form" name="form" action="" method="post">
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <div>
        <label for="title">Title: </label>
        <input type="text" name="event_title" id="event_title" required autofocus/>
    </div>
    <div>
        <label for="content">Content: </label>
        <input type="text" name="event_content" id="event_content" required/>
    </div>
    <div>
        <label for="description">Description: </label>
        <input type="text" name="event_description" id="event_description" required/>
    </div>
   
    <button type="submit">Save</button>
</form>

</body>
<html>