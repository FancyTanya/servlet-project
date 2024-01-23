<%--
  Created by IntelliJ IDEA.
  User: tanya
  Date: 1/21/24
  Time: 2:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Search Warehouses</title>

</head>
<body>

<h2>Search Warehouses</h2>

<form action="searchWarehouses.jsp" method="post">
    <label>ID: </label>
    <label>
        <input type="text" name="id" />
    </label>
    <br/>
    <label>Name: </label>
    <label>
        <input type="text" name="name" />
    </label>
    <br/>
    <label>Address Line 1: </label>
    <label>
        <input type="text" name="addressLine1" />
    </label>
    <br/>
    <label>Address Line 2: </label>
    <label>
        <input type="text" name="addressLine2" />
    </label>
    <br/>
    <label>City: </label>
    <label>
        <input type="text" name="city" />
    </label>
    <br/>
    <label>State: </label>
    <label>
        <input type="text" name="state" />
    </label>
    <br/>
    <label>Country: </label>
    <label>
        <input type="text" name="country" />
    </label>
    <br/>
    <label>Inventory Quantity: </label>
    <label>
        <input type="text" name="inventoryQuantity" />
    </label>
    <br/>
    <label>Limit: </label>
    <label>
        <input type="text" name="limit" />
    </label>
    <br/>
    <label>Offset: </label>
    <label>
        <input type="text" name="offset" />
    </label>
    <br/>
    <input type="submit" value="Search" onclick="search()" />


</form>

</body>
</html>
