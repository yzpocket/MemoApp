package com.multicamp;

import java.awt.*;
import java.util.Date;
import javax.swing.*;
import java.text.SimpleDateFormat;
/* MVC패턴 독립적인 앱들을 개발하던 디자인 패턴인데 웹에도 도입하고 있다.
 * 모델1방식 : mvc패턴을 적용하지 않을 때
 * 모델2방식 : mvc패턴을 적용
 * 이렇게 M/V/C계층을 분업화해서 개발하는 패턴. 
 * 
 * M = Model		: - DB접근해서 로직(비즈니스 로직을 갖는다. CRUD)  
 * 						- VO객체들 [Domain Model Layer]
 * 						-DAO(Data Access Object, 실제적으로 DB에 접근하는 객체) 클래스   [Persistence Layer] 
 * V = View			: [Presentatioin Layer] 
 * 						- 화면단 / 지금은 Swing으로 한다, 웹에서는 HTML(JSP)
 * C = Controller	:  Model과 View 사이에 제어하는 역할을 담당하는 계층
 * 						(여기선 Event Handler 비슷한거)
 * 						웹에서는 Servlet/SpringFramework의 Controller를 사용
 * 
 * 
 * 
 * 아래 MemoApp:=> GUI /(V)iew 담당 [Presentatioin Layer] 이다.
 * 
 * XXXDAO: DB접근 로직(비즈니스 로직을 갖는다. CRUD)
 * 		   Data Access Object  [Persistence Layer-영속성 계층]
 * XXXVO/XXXDTO [Domain Layer]
 *  Value Object/ Data Transfer Object
 * 	: 사용자가 입력한 값을 담거나 DB에서 가져온 값을 갖고 있는
 *    객체
 * 
 * */
public class MemoApp extends JFrame {

	JLabel lb;
	JPanel p=new JPanel(new GridLayout(2,1));
	JPanel p_1=new JPanel();
	JPanel p4=new JPanel();
	
	JTextArea ta;
	JTextField tfName, tfDate, tfMsg, tfIdx;
	JButton btAdd, btList, btDel, btEdit, btEditEnd, btFind;
	MemoHandler handler;
//	MemoDAO dao;
	
//	MemoFindGui subFrame=new MemoFindGui(this);
	
	public MemoApp() {
		super("::MemoApp::");
//		dao=new MemoDAO();
		
		Container cp=this.getContentPane();
		cp.add(p,"North");
		
		ta=new JTextArea("::한줄 메모장::");
		cp.add(new JScrollPane(ta),"Center");
		ta.setEditable(false);//편집 못하도록. 읽기 전용
		
		lb=new JLabel("♥♥한줄 메모장♥♥", JLabel.CENTER);
		p.add(lb);
		lb.setFont(new Font("Serif",Font.BOLD,28));
		p.add(p_1);
		
		JPanel p2=new JPanel();
		JPanel p3=new JPanel();
		
		
		p_1.setLayout(new GridLayout(2,1));
		p_1.add(p2);
		p_1.add(p3);
		
		p2.setLayout(new FlowLayout(FlowLayout.LEFT));
		p3.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		p2.add(new JLabel("작성자: "));
		tfName=new JTextField(12);
		p2.add(tfName);
		
		p2.add(new JLabel("작성일: "));
		tfDate=new JTextField(12);
		p2.add(tfDate);
		
		p2.add(new JLabel("글번호: "));
		tfIdx=new JTextField(12);
		p2.add(tfIdx);
		tfIdx.setEditable(false);
		
		
		p3.add(new JLabel("메모내용: "));
		tfMsg=new JTextField(40);
		p3.add(tfMsg);
		
		btAdd=new JButton("등  록");
		btList=new JButton("글목록");
		p3.add(btAdd);
		p3.add(btList);
		
		tfDate.setEditable(false);
		tfDate.setText("YY-MM-DD");
		tfDate.setFont(new Font("Dialog",Font.BOLD,14));
		tfDate.setForeground(Color.blue);
		tfDate.setHorizontalAlignment(tfDate.CENTER);
		
		cp.add(p4,"South");
		btDel=new JButton("글삭제");
		btEdit=new JButton("글수정");
		btEditEnd=new JButton("글수정 처리");
		btFind=new JButton("글검색");
		
		p4.add(btDel);
		p4.add(btEdit);
		p4.add(btEditEnd);
		p4.add(btFind);
		
		String today=getDate(); //화면에 날짜 넣기.
		tfDate.setText(today);
		
		//리스너 부착----
		handler=new MemoHandler(this);
		btAdd.addActionListener(handler);
		btList.addActionListener(handler);
		btDel.addActionListener(handler);
		btEdit.addActionListener(handler);
		btEditEnd.addActionListener(handler);
		btFind.addActionListener(handler);
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(750,500);
		setVisible(true);
	}//생성자-----------
	
	//현재날짜를 YY/MM/DD 포맷의 문자열로 반환하는 메서드 //화면에 날짜 넣기.
	public String getDate() {
		Date today=new Date();
//		System.out.println(today.toString());
		SimpleDateFormat sdf=new SimpleDateFormat("yy-MM-dd");
//		SimpleDateFormat sdf=new SimpleDateFormat("yy-MM-dd hh:mm:ss"); 
											//java 		yy연-MM월-dd일 hh시간:mm분:ss초
											//oracle	yy연-mm월-dd일 hh시간:mi분:ss초 양식이 좀 다르다.
		String str=sdf.format(today);
//		System.out.println(str); //확인용.
		return str;
	}
	
	public void showMessage(String str) {
		JOptionPane.showMessageDialog(this, str);
	}//----------------------------
	
	
	public static void main(String[] args) {
		new MemoApp();
	}
	public void clearTf() {
		tfIdx.setText("");
		tfName.setText("");
		tfMsg.setText("");
		tfName.requestFocus();
	}
	public void showTextArea(List<MemoVO> arr) {
		if(arr==null||arr.size()==0) {
			ta.setText("데이터가 없습니다.");
		}else {
			ta.setText("");
			ta.append("===============================\n");
			ta.append("글번호\t작성자\t메모내용\t\t\t작성일\n");
			ta.append("===============================\n");
			for(MemoVO vo:arr) {
				ta.append(vo.getIdx()+"\t"+vo.getName()+vo.getMsg()+vo.getWdate()+"\n");

			}
			ta.append("========================\n");
		}
		ta.setCaretPosition(0); //커서위치 앞으로
	}

	public String showInput(String str) {
		String str=JOptionPane.showInputDialog(msg);
		return str;
	}
}





