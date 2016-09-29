<#-- @ftlvariable name="_csrf" type="org.springframework.security.web.csrf.CsrfToken" -->
<#-- @ftlvariable name="currentUser" type="eu.kielczewski.example.domain.CurrentUser" -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Home page</title>
</head>
<body>
<nav role="navigation">
    <ul>
    <#if !currentUser??>
        <li><a href="/login">Log in</a></li>
    </#if>
    <#if currentUser??>
        <li>
            <form action="/logout" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit">Log out</button>
            </form>
        </li>
        <li><a href="/user/${currentUser.id}">View myself</a></li>
    </#if>
	<!-- If currentUser exist -->
    <#if currentUser?? && currentUser.role == "ADMIN">
        <li><a href="/user/create">Create a new user</a></li>
        <li><a href="/users">View all users</a></li>
        <li><a href="/viewAuditLogPage">View audit log selection page</a></li>
    </#if>
<!-- If role == "USER" -->
<#if currentUser?? && (currentUser.role == "USER" || currentUser.role == "ADMIN")>
        <li><a href="/user/send">Send Message</a></li>
        <li><a href="/user/messages">View Messages</a></li>
    </#if>
<!-- If role == "EVENT"  -->
	 <#if currentUser?? && currentUser.role == "EVENT">
			<li><a href="/event/create">Create A Event</a></li>
			<li><a href="/event/view">View All Events</a></li>
	</#if>

    </ul>
</nav>
</body>
</html>