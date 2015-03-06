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
