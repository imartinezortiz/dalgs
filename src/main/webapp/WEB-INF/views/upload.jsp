<%@ include file="/WEB-INF/views/include.jsp"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Upload CSV</title>
</head>

<body>
	<div class="list-group index">


		<div class="panel panel-primary group">
			<div class="panel-heading">
				<h3 class="panel-title list">Upload a ${className} CSV File  </h3>

			</div>

			<div class="panel-body">
				<form:form commandName="newUpload" method="post"
					enctype="multipart/form-data">
					
					<br>
					<div class="form-group view">
						<label>Encoding</label><br>
						<form:select path="charset">
							<form:option value="UTF-8">UTF-8</form:option>
							<form:option value="">--------------------</form:option>
							
							<form:options items="${listCharsets}" />
						</form:select>
						
						

					</div>
					<br>
					<div class="form-group view">
						<label>File Syntax </label>

						<table>
							<tr>
								<td><p>Quote Char</p></td>
								<td><form:input path="quoteChar" type="text" value='\"' /></td>
							</tr>
							<tr>
								<td><p>Delimiter Char</p></td>
								<td><form:input path="delimiterChar" type="text" value=',' /></td>
							</tr>
							<tr>
								<td><p>End of Line</p></td>
								<td><form:input path="endOfLineSymbols" type="text"
										value='\r\n' /></td>
							</tr>
						</table>
					</div>
					<br>
					<div class="form-group view">
						<label>CSV File</label>
						<form:input type="file" path="fileData" class="filestyle"
							data-classButton="btn btn-primary" data-input="true"
							data-classIcon="icon-plus" data-buttonText="  Choose a file" />

					</div>

					<br>
					<input class="btn btn-success" type="submit" value="Upload File" />

				</form:form>
			</div>

			<div class="panel-body">
				<table border="0" class="table table-striped">
					<h4 style="text-align: center;">Most Common delimiters:</h4>
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
					<tr>
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
				</table>
			</div>
		</div>
	</div>
</body>
</html>