package com.redlichee.uwinmes.application;

public class Config {

	public static boolean IS_RELEASE = false;// 判断是否是发布版true:发布 false:测试

	public static String URL_WEBSERVICE = "http://120.25.165.32:8099/QcWebService.asmx";

	public static String URL_SERVICE_IP = "http://120.25.165.32:8099";
	public static String URL_SERVICE_IP_TEST = "http://120.25.165.32:8099";
	public static String URL_ASMX = "/QcWebService.asmx";
	public static String URL_NAME_SPACE = "http://tempuri.org/";


	//**************start**********************配置Key*********************start********************
	//**************start**********************配置Key*********************start********************

	public static final String FILE = "config";
	public static final String PLIS_NAME = "redlichee_uwinmes";
	public static final String TOKEN_ID = "koken_id";
	public static final String ACCOUNT = "account";//用户账号
	public static final String PASSWORD = "passeword";//用户密码
	public static final String IS_SAVE_PSW = "issavepssward";
	public static final String PHONE_NUM = "phoneNum";
	public static final String IS_ADMIN = "isAdmin";//是否是管理员
	public static final String IS_LEADER = "isLeader";//是否是领导

	//用户信息
	public static final String USER_NAME = "userName";//用户名
	public static final String PERSON_ID = "personID";//用户ID
	public static final String TEL_NOMBER = "telNomber";//用户手机号码
	public static final String E_MAIL = "e_mail";//用户邮箱
	public static final String DEPARTMENT = "department";//用户部门
	public static final String DEPT_NUMBER = "Dept_Number";//用户部门号
	public static final String EMPLOYEE_NUM = "employee_num";//用户工号
	public static final String COMPANY_NAME = "companyName";//用户公司名称
	public static final String COMPANY_PK = "companyPk";//用户公司ID
	public static final String RECOMMAND_CODE = "recommandCode";//用户推荐码

	public static final String SERVER_ADDRESS = "server_address";//存服务器地址
	//**************end*********************配置Key************************end**********************
	//**************end*********************配置Key************************end**********************



	//**************start**********************URL*********************start************************
	//**************start**********************URL*********************start************************

	// 红荔网平台服务协议
	public static final String URL_EXEMPT = "http://index.redlichee.com/agreement.jsp";

	//图片URL
	public static final String IMG_URL = "file/queryImg?imgId=";

	//缩略图片URL
	public static final String IMG_THUMBNAIL_URL = "file/queryThumbnail?imgId=";

	// 登录
	public static final String URL_LOGIN = "Login"; // 登录
	public static final String URL_LOGOUT = "mAccount/logout"; // 注销

	//
	public static final String URL_PROCESS_FLOW = "GetProcessFlow"; // 获取待检验质检单

	public static final String URL_SOLUTION_LIST = "GetSolutionList"; // 获取检验方案列表

	public static final String URL_QC_LIST = "CreateQcList"; // 生产质检单信息

	public static final String URL_CRUDQC = "CRUDQc"; // 编辑保存质检单

	public static final String URL_GET_QC_LIST = "GetQcList"; // 根据主键获取质检单信息

	public static final String URL_DELETE_VOCUCHER = "DeleteVocucher"; // 删除质检单

	public static final String URL_GET_BASEDATA = "GetBaseData"; // 获取部门，生产线，标准工序

	//**************end*********************URL************************end**************************
	//**************end*********************URL************************end**************************

}
