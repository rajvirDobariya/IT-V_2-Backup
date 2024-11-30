package com.suryoday.uam.pojo;


public class UserAccessResponse {

		private String moduleName;
		private String icon;
		public String getModuleName() {
			return moduleName;
		}
		public void setModuleName(String moduleName) {
			this.moduleName = moduleName;
		}
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
		public UserAccessResponse(String moduleName, String icon) {
			super();
			this.moduleName = moduleName;
			this.icon = icon;
		}
		public UserAccessResponse() {
			super();
			// TODO Auto-generated constructor stub
		}
		@Override
		public String toString() {
			return "UserAccessResponse [moduleName=" + moduleName + ", icon=" + icon + "]";
		}
		
		
}
