package DAO;

import helper.Constants.Constants;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Reports;
import service.ReportService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLReportDAO  implements ReportService{
	/**
	 * Method to call the class to call the execute method
	 * @return List
	 * @throws SQLException
	 */
	public static ObservableList<Reports> getCustReports() throws SQLException{
		FindCustReport fcr = new FindCustReport();
		return fcr.execute();
	}

	private static class FindCustReport {
		/**
		 * Execute method creates the sql string to pass into the sql database.
		 *
		 * @return
		 * @throws SQLException
		 */
		public  ObservableList<Reports> execute()throws SQLException{
			String sql = "SELECT MONTH(Start) as Month, Type, COUNT(*) as Number from appointments GROUP BY Month, Type";
			PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			String month = " ";
			ObservableList<Reports> repList = FXCollections.observableArrayList();
			while(rs.next()){
				Reports rep = new Reports();
				// gets the month by an int from the sql table
				int monthInt = rs.getInt(1);

				rep.setReportAppType(rs.getString(2));
				rep.setReportTotal(rs.getInt(3));
				// uses a switch statement to get the correct string for the month
				switch(monthInt){
					case Constants.JAN_INT:
						month = Constants.JANUARY;
						break;
					case Constants.FEB_INT:
						month = Constants.FEBRUARY;
						break;
					case Constants.MAR_INT:
						month = Constants.MARCH;
						break;
					case Constants.APR_INT:
						month = Constants.APRIL;
						break;
					case Constants.MAY_INT:
						month = Constants.MAY;
						break;
					case Constants.JUNE_INT:
						month = Constants.JUNE;
						break;
					case Constants.JULY_INT:
						month = Constants.JULY;
						break;
					case Constants.AUG_INT:
						month = Constants.AUGUST;
						break;
					case Constants.SEP_INT:
						month = Constants.SEPTEMBER;
						break;
					case Constants.OCT_INT:
						month = Constants.OCTOBER;
						break;
					case Constants.NOV_INT:
						month = Constants.NOVEMBER;
						break;
					case Constants.DEC_INT:
						month = Constants.DECEMBER;
						break;

				}
				rep.setReportAppMonth(month);
				repList.addAll(rep);
			}
			return repList;
		}
	}


}
