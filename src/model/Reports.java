package model;

import java.time.LocalDateTime;

public class Reports {

	private int reportCustId;
	private String reportCustName;
	private int reportAppId;
	private String reportAppType;
	private String reportAppMonth;
	private int reportContId;
	private String reportAppDesc;
	private LocalDateTime reportAppStartDt;
	private LocalDateTime reportAppEndDt;
	private String reportAppTitle;
	private int reportTotal;

	public String getReportAppTitle() {
		return reportAppTitle;
	}

	public void setReportAppTitle(String reportAppTitle) {
		this.reportAppTitle = reportAppTitle;
	}


	public String getReportAppDesc() {
		return reportAppDesc;
	}

	public void setReportAppDesc(String reportAppDesc) {
		this.reportAppDesc = reportAppDesc;
	}

	public LocalDateTime getReportAppStartDt() {
		return reportAppStartDt;
	}

	public void setReportAppStartDt(LocalDateTime reportAppStartDt) {
		this.reportAppStartDt = reportAppStartDt;
	}

	public LocalDateTime getReportAppEndDt() {
		return reportAppEndDt;
	}

	public void setReportAppEndDt(LocalDateTime reportAppEndDt) {
		this.reportAppEndDt = reportAppEndDt;
	}

	public int getReportContId() {
		return reportContId;
	}

	public void setReportContId(int reportContId) {
		this.reportContId = reportContId;
	}

	public int getReportCustId() {
		return reportCustId;
	}

	public void setReportCustId(int reportCustId) {
		this.reportCustId = reportCustId;
	}

	public String getReportCustName() {
		return reportCustName;
	}

	public void setReportCustName(String reportCustName) {
		this.reportCustName = reportCustName;
	}

	public int getReportAppId() {
		return reportAppId;
	}

	public void setReportAppId(int reportAppId) {
		this.reportAppId = reportAppId;
	}

	public String getReportAppType() {
		return reportAppType;
	}

	public void setReportAppType(String reportAppType) {
		this.reportAppType = reportAppType;
	}

	public String getReportAppMonth() {
		return reportAppMonth;
	}

	public void setReportAppMonth(String reportAppMonth) {
		this.reportAppMonth = reportAppMonth;
	}

	public int getReportTotal() {
		return reportTotal;
	}

	public void setReportTotal(int reportTotal) {
		this.reportTotal = reportTotal;
	}
}
