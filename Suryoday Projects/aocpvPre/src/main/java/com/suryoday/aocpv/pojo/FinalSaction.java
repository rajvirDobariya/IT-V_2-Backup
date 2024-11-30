package com.suryoday.aocpv.pojo;

public class FinalSaction {
		
		private String checklistname;
		private String result;
		private String Remarks;
		public String getChecklistname() {
			return checklistname;
		}
		public void setChecklistname(String checklistname) {
			this.checklistname = checklistname;
		}
		public String getResult() {
			return result;
		}
		public void setResult(String result) {
			this.result = result;
		}
		public String getRemarks() {
			return Remarks;
		}
		public void setRemarks(String remarks) {
			Remarks = remarks;
		}
		public FinalSaction(String checklistname, String result, String remarks) {
			super();
			this.checklistname = checklistname;
			this.result = result;
			Remarks = remarks;
		}
		public FinalSaction() {
			super();
			// TODO Auto-generated constructor stub
		}
		
}
