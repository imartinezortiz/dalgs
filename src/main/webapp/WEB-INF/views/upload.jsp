<%@ include file="/WEB-INF/views/include.jsp"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title><fmt:message key="upload" /></title>
</head>

<body>
	<div class="list-group index">


		<div class="panel panel-primary group">
			<div class="panel-heading">
				<h3 class="panel-title list"><fmt:message key="upload" /> : ${className}</h3>

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
						<label><fmt:message key="syntax" /></label>

						<table>
							<tr>
								<td><p><fmt:message key="quote" /></p></td>
								<td><form:input path="quoteChar" type="text" value='\"' /></td>
							</tr>
							<tr>
								<td><p><fmt:message key="delimiter" /></p></td>
								<td><form:input path="delimiterChar" type="text" value=',' /></td>
							</tr>
							<tr>
								<td><p><fmt:message key="eol" /></p></td>
								<td><form:input path="endOfLineSymbols" type="text"
										value='\r\n' /></td>
							</tr>
						</table>
					</div>
					<br>
					<div class="form-group view">
					
					<spring:message code="choosefile" var="choose"/>
					<spring:message code="upload" var="upload"/>
					
						<label><fmt:message key="file" /></label>
						<form:input type="file" path="fileData" class="filestyle"
							data-classButton="btn btn-primary" data-input="true"
							data-classIcon="icon-plus" data-buttonText="${choose}" />

					</div>

					<br>
					<input class="btn btn-success" type="submit" value="${upload}" />

				</form:form>
			</div>

			<div class="panel-body">
				<table border='0' class="table table-striped">
					<h4 style="text-align:center;"><fmt:message key="commonDel" /></h4>
					<tr>
						<td align="left"><b><fmt:message key="constant" /></b></td>
						<th align="center">	<fmt:message key="quote" /></th>
						<th align="center"><fmt:message key="delimiter" /></th>
						<th align="center"><fmt:message key="eol" /></th>
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