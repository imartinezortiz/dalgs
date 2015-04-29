/**
 * This file is part of D.A.L.G.S.
 *
 * D.A.L.G.S is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * D.A.L.G.S is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with D.A.L.G.S.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.dalgs.classes;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class UploadForm {

	private CommonsMultipartFile fileData;
	private String nameClass;

	/* Delimiter components */
	private String quoteChar;
	private String delimiterChar;
	private String endOfLineSymbols;
	private String charset;

	public UploadForm() {
		super();
	}

	public UploadForm(String nameClass) {
		super();
		this.nameClass = nameClass;
	}

	public String getQuoteChar() {
		return quoteChar;
	}

	public void setQuoteChar(String quoteChar) {
		this.quoteChar = quoteChar;
	}

	public String getDelimiterChar() {
		return delimiterChar;
	}

	public void setDelimiterChar(String delimiterChar) {
		this.delimiterChar = delimiterChar;
	}

	public String getEndOfLineSymbols() {
		return endOfLineSymbols;
	}

	public void setEndOfLineSymbols(String endOfLineSymbols) {
		this.endOfLineSymbols = endOfLineSymbols;
	}

	public String getNameClass() {
		return nameClass;
	}

	public void setNameClass(String nameClass) {
		this.nameClass = nameClass;
	}

	public CommonsMultipartFile getFileData() {
		return fileData;
	}

	public void setFileData(CommonsMultipartFile fileData) {
		this.fileData = fileData;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

}
