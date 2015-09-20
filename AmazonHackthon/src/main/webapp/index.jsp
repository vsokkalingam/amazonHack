<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script type="text/javascript" language="javascript"
	src="http://www.technicalkeeda.com/js/javascripts/plugin/jquery.js"></script>
<script type="text/javascript"
	src="http://www.technicalkeeda.com/js/javascripts/plugin/json2.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Product search</title>
<style>
Table.GridOne {
	padding: 3px;
	margin: 0;
	background: lightyellow;
	border-collapse: collapse;
	width: 35%;
}

Table.GridOne Td {
	padding: 2px;
	border: 1px solid #ff9900;
	border-collapse: collapse;
}
</style>
<script type="text/javascript">
	function subscribe() {
		var json = {
			productID : $("#productID").val(),
			releaseDate : $("#releaseDate").val(),
			title : $("#title").val(),
			publiser : $("#publiser").val(),
			author : $("#author").val(),
			minlevel : $("#minlevel").val(),
			maxlevel : $("#maxlevel").val(),
		};
		$.post("/AmazonHackthon//main/subscirption", json, function(
				responseData) {
			alert("Subscribed successfuly");
		});
	}
	function search() {
		var json = {
			productId : $("#productIDAlone").val()
		}
		$.post("/AmazonHackthon/main/search", json, function(
				responseData) {
			alert(responseData);
		});
	}
	function addAttributes() {
		var json = {
			attrName : $("#attrName").val(),
			attrValue : $("#attrValue").val(),
			mailAddress : $("#mailAddress2").val()
		};
		$.post("/AmazonHackthon/main", json, function(obj) {
			alert("Subscribed successfuly");
		});

	}
	
</script>
</head>
<body>
	<h1>Product search</h1>
	<form name="productSearch" method="post">
		<table cellpadding="0" cellspacing="0" border="1" class="GridOne">
			<tr>
				<td>Product ID</td>
				<td><input type="text" name="productID" id="productID" value=""></td>
			</tr>
			<tr>
				<td>Release Date</td>
				<td><input type="text" name="releaseDate" id="releaseDate"
					value=""></td>
			</tr>
			<tr>
				<td>Title</td>
				<td><input type="text" name="title" id="title" value=""></td>
			</tr>
			<tr>
				<td>Publisher</td>
				<td><input type="text" name="publiser" id="publiser" value=""></td>
			</tr>
			<tr>
				<td>Author</td>
				<td><input type="text" name="author" id="author" value=""></td>
			</tr>
			<tr>
				<td>Price Min level</td>
				<td><input type="text" name="minlevel" id="minlevel" value=""></td>
			</tr>
			<tr>
				<td>Price Min level</td>
				<td><input type="text" name="maxlevel" id="maxlevel" value=""></td>
			</tr>
			<tr>
				<td>Mail Address</td>
				<td><input type="text" name="mailAddress" id="mailAddress" value=""></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="button"
					value="Subscription" onclick="subscribe();"></td>
			</tr>
		</table>
	</form>

	

<h1>Subcription new fields  </h1>
	<form name="subcriptionExtra" method="post">
		<table cellpadding="0" cellspacing="0" border="1" class="GridOne">
			<tr>
				<td>Attribute name</td>
				<td><input type="text" name="attrName" id="attrName" value=""></td>
			</tr>
			<tr>
				<td>Attribute value</td>
				<td><input type="text" name="attrValue" id="attrValue" value=""></td>
			</tr>
			<tr>
				<td>Mail Address</td>
				<td><input type="text" name="mailAddress" id="mailAddress2" value=""></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="button"
					value="Submit" onclick="addAttributes();"></td>
			</tr>
		</table>
	</form>
<h1>Product ID </h1>
	<input type="text" name="productID" id="productIDAlone" value="">
	<input type="button" value="Search" onclick="search();">
	<div id="result"></div>
</body>
</html>