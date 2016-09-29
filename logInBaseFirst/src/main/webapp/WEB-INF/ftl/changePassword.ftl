<#-- @ftlvariable name="_csrf" type="org.springframework.security.web.csrf.CsrfToken" -->
<#-- @ftlvariable name="form" type="eu.kielczewski.example.domain.UserCreateForm" -->
<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="en">
<head>
   
    <title>Change Password Page</title>
</head>
<body>
<h1>Reset your password</h1>

<form role="form" name="form" action="" method="post">
  	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <div>
        <label for="password">Enter New Password</label>
        <input type="password" name="password1" id="password" required autofocus/>
        <label for="password">Enter Password Again</label>
        <input type="password" name="password2" id="password2" required />
    </div>

    <button type="submit">Change Password</button>
</form>

</body>
 
</html>