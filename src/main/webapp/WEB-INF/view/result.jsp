<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html;charset=UTF-8" %>


<html>
<head>
    <title>Результати пошуку по складам</title>

</head>
<body>

<h2>Результати пошуку по складам</h2>

<c:forEach var="warehouse" items="${requestScope.warehouses}">
    <ul>
        Назва: <c:out value="${warehouse.name}"/> <br>
        Адреса1: <c:out value="${warehouse.addressLine1}"/> <br>
        Адреса2: <c:out value="${warehouse.addressLine2}"/> <br>
        Місто: <c:out value="${warehouse.city}"/> <br>
        Штат: <c:out value="${warehouse.state}"/> <br>
        Країна: <c:out value="${warehouse.country}"/> <br>
        Кількість продуктів: <c:out value="${warehouse.inventoryQuantity}"/> <br>


        <form method="get" action="<c:url value='/update_warehouse'/>">
            <label>
                <input type="number" hidden name="id" value="${warehouse.id}"/>
            </label>
            <input type="submit" value="Редагувати"/>
        </form>
        <form method="post" action="<c:url value='/delete_warehouse'/>">
            <label>
                <input type="number" hidden name="id" value="${warehouse.id}"/>
            </label>
            <input type="submit" name="delete" value="Видалити"/>
        </form>


    </ul>
    <hr/>

</c:forEach>

<h2>Додати новий склад</h2>
<form method="post" action="<c:url value='/add_warehouse'/>">

    <label>Назва <input type="text" name="name" required></label><br>
    <label>Адреса1 <input type="text" name="addressLine1" required></label><br>
    <label>Адреса2 <input type="text" name="addressLine2" required></label><br>
    <label>Місто <input type="text" name="city" required></label><br>
    <label>Штат <input type="text" name="state" required></label><br>
    <label>Країна <input type="text" name="country" required></label><br>
    <label>Кількість продуктів <input type="text" name="inventoryQuantity" required></label><br>

    <input type="submit" value="Ok" name="Ok"><br>
</form>

<br><br>

</body>
</html>
