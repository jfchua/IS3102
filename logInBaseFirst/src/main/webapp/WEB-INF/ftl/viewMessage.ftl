<#-- @ftlvariable name="users" type="java.util.List<eu.kielczewski.example.domain.User>" -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Your current Messages</title>
</head>
<body>
<nav role="navigation">
    <ul>
        <li><a href="/">Home</a></li>
    </ul>
</nav>

<h1>Your current messages</h1>

<table>
    <thead>
    <tr>
        <th>From: </th>
        <th>Message</th>
    </tr>
    </thead>
    <tbody>
    <#list messages as message>
    <tr>
    	<td>${message.sender}</td>
        <td>${message.message}</td>
    </tr>
    </#list>
    </tbody>
</table>
</body>
</html>