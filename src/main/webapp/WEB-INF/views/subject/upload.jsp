<%@ include file="/WEB-INF/views/include.jsp"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>User</title>
</head>

<body><div class="list-group index">
	

		<div class="panel panel-primary group">
			<div class="panel-heading">
				<h3 class="panel-title list">Upload a CSV File</h3>

			</div>

			<div class="panel-body">
		<form action="tfg/upload.htm" method="post"
			enctype="multipart/form-data">
<div class="form-group view">
			<label>Entity Class</label><br> <input type="text" name="typeClass" />
			</div><div class="form-group view">
			<label>Delimiter </label> 
			<br>
			
			<select>
				<option id='pref1' name="delimiter" value='CsvPreference.STANDARD_PREFERENCE'>CsvPreference.STANDARD_PREFERENCE</option>
				<option id='pref2'  name="delimiter" value='CsvPreference.EXCEL_PREFERENCE'>CsvPreference.EXCEL_PREFERENCE</option>
				<option id='pref3'  name="delimiter" value='CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE'>CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE</option>
				<option id='pref4'  name="delimiter" value='CsvPreference.TAB_PREFERENCE'>CsvPreference.TAB_PREFERENCE</option>
			</select> 
			</div>
			<div class="form-group view">
		<label>CSV File</label><input  type="file" name="file" size="50" />
		</div>
		
		<br>
		 <input class="btn btn-success" type="submit"
				value="Upload File" />
				
		</form>
	</div>

<div class="panel-body">
	<table border="0" class="table table-striped">
	<h4 style="text-align: center;">Different type of super-csv delimiters:</h4>
		<tr>
			<td align="left"><b>Constant</b></td>
			<th align="center">Quote char</th>
			<th align="center">Delimiter char</th>
			<th align="center">End of line symbols</th>
		</tr>
		<tr>
			<td align="left"><a
				href="./apidocs/org/supercsv/prefs/CsvPreference.html#STANDARD_PREFERENCE">STANDARD_PREFERENCE</a></td>
			<td align="center">&quot;</td>
			<td align="center">,</td>
			<td align="center">\r\n</td>
		</tr>
		<tr>
			<td align="left"><a
				href="./apidocs/org/supercsv/prefs/CsvPreference.html#EXCEL_PREFERENCE">EXCEL_PREFERENCE</a></td>
			<td align="center">&quot;</td>
			<td align="center">,</td>
			<td align="center">\n</td>
		</tr>
		<tr >
			<td align="left"><a
				href="./apidocs/org/supercsv/prefs/CsvPreference.html#EXCEL_NORTH_EUROPE_PREFERENCE">EXCEL_NORTH_EUROPE_PREFERENCE</a></td>
			<td align="center">&quot;</td>
			<td align="center">;</td>
			<td align="center">\n</td>
		</tr>
		<tr>
			<td align="left"><a
				href="./apidocs/org/supercsv/prefs/CsvPreference.html#TAB_PREFERENCE">TAB_PREFERENCE</a></td>
			<td align="center">&quot;</td>
			<td align="center">\t</td>
			<td align="center">\n</td>
		</tr>
	</table></div>
	</div></div>
</body>
</html>