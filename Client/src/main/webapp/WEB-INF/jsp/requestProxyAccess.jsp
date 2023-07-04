<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Get Time</title>
</head>
<body>
<h3 style="color: red;">Get Time</h3>

<%--pretty sure khabiir copied this from somewhere, but i cannot find the original --%>
<div>
    <form:form action="http://localhost:8080/oauth/authorize"
               method="post" modelAttribute="emp">
    <p>
        <label>Get Time</label>
        <input type="text" name="response_type" value="code"/>
        <input type="text" name="client_id" value="timeservice-client"/>
        <input type="text" name="redirect_uri" value="http://localhost:8090/showTime"/>
        <input type="text" name="scope" value="read"/>
        <input type="SUBMIT" value="Get Time"/>
        </form:form>
</div>
</body>
</html>