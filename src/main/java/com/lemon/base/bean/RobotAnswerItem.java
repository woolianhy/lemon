package com.lemon.base.bean;

public class RobotAnswerItem {
    
	/*
	 * 航班
	 */
	//航班
	private String flight;
	//航班路线
	private String route;
	
	private String starttime;
	
	private String endtime;
	//航班状态
	private String state;
	
	/*
	 * 新闻
	 */
	//标题
	//来源
	private String article;
	
	private String source;
	
	
	/*
	 * 列车
	 */
	//车次
	private String trainnum;
	//起始站
	private String start;
	//到达站
	private String terminal;
	
	/*
	 * 菜谱
	 */
	//名称
	private String name;
	//详情
	private String info;
	
	
	//详情地址
	private String detailurl;
	//图标地址
	private String icon;

	public String getFlight() {
		return flight;
	}

	public void setFlight(String flight) {
		this.flight = flight;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDetailurl() {
		return detailurl;
	}

	public void setDetailurl(String detailurl) {
		this.detailurl = detailurl;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTrainnum() {
		return trainnum;
	}

	public void setTrainnum(String trainnum) {
		this.trainnum = trainnum;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
