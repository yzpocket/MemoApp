package com.multicamp;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
//Controller
public class MemoHandler implements ActionListener{
	
	MemoApp app;//View
	MemoDAO dao=new MemoDAO();//Model
	
	public MemoHandler(MemoApp app) {
		this.app=app;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o=e.getSource();
		if(o==app.btAdd) {
			//app.setTitle("Add");
			addMemo();
			app.clearTf();
			listMemo();
		}else if(o==app.btList) {
			listMemo();
		}else if(o==app.btDel) {
			deleteMemo();
			listMemo();
		}else if(o==app.btEdit) {
			editMemo();//수정글을 입력폼에 보여주기
		}else if(o==app.btEditEnd) {
			editMemoEnd();//수정처리
			listMemo();
		}else if(o==app.btFind) {
			app.subFrame.setLocation(app.getWidth()-5,0);//x,y
			app.subFrame.setVisible(true);
			
		}else if(o==app.subFrame.btFindEnd || o==app.subFrame.tfKeyword ) {
			//app.setTitle("###");
			findMemo();
		}
	}//-----------------------------------
	
	public void findMemo() {
		//검색 유형 얻어오기
		int type=app.subFrame.comboType.getSelectedIndex();
		System.out.println("type: "+type);
		//검색어 얻어오기
		String keyword=app.subFrame.tfKeyword.getText();
		if(keyword==null||keyword.trim().isBlank()) {
			app.showMessage("검색할 키워드를 입력하세요");
			app.subFrame.tfKeyword.requestFocus();
			return;
		}
		try {
			List<MemoVO> arr=dao.findMemo(type, keyword);
			if(arr==null||arr.size()==0) {
				app.showMessage("검색한 글이 없어요");
				return;
			}
			//app.showTextArea(arr);
			app.subFrame.showTable(arr);
		}catch(SQLException  e) {
			e.printStackTrace();
			app.showMessage(e.getMessage());
		}
	}//-------------------------------
	
	public void editMemo() {//select문 where절
		String idxStr=app.showInput("수정할 글 번호를 입력하세요");
		if(idxStr==null) return;
		//글번호(PK)
		try {
			MemoVO vo=dao.selectMemo(Integer.parseInt(idxStr.trim()));
			if(vo==null) {
				app.showMessage(idxStr+"번 글은 존재하지 않아요");
				return;
			}
			app.setText(vo);
		}catch(SQLException e) {
			app.showMessage(e.getMessage());
			e.printStackTrace();
		}
	}//-----------------------------
	public void editMemoEnd() {//update문
		//1. 사용자가 입력한 값 얻어오기(글번호,작성자,메모내용)
		String idxStr=app.tfIdx.getText();
		String name=app.tfName.getText();
		String msg=app.tfMsg.getText();
		//2. 유효성 체크(글번호, 작성자)
		if(idxStr==null||name==null||idxStr.trim().isEmpty()||name.trim().isEmpty()) {
			app.showMessage("글번호와, 작성자는 반드시 입력해야 해요");
			return;
		}
		int idx=Integer.parseInt(idxStr.trim());
		
		//3. 1번에서 얻어온 값들을 MemoVO에 담아주기
		MemoVO vo=new MemoVO(idx,name,msg,null);
		try {
			//4. dao의 수정처리 메서드 호출하기
			int n=dao.updateMemo(vo);
			//5. 그 실행결과에 따른 메시지 처리
			String str=(n>0)?"글 수정 성공":"글 수정 실패";
			app.showMessage(str);
		}catch(SQLException e) {
			e.printStackTrace();
			app.showMessage(e.getMessage());
		}
	}//-----------------------------
	
	public void deleteMemo() {//delete문
		String idxStr=app.showInput("삭제할 글 번호를 입력하세요");
		if(idxStr==null) return;
		try {
			int n=dao.deleteMemo(Integer.parseInt(idxStr.trim()));
			String str=(n>0)?"글 삭제 성공":"글 삭제 실패";
			app.showMessage(str);
		}catch(SQLException e) {
			e.printStackTrace();
			app.showMessage(e.getMessage());
		}
	}//----------------------------------
	
	
	public void listMemo() {//select문
		try {
			List<MemoVO> arr=dao.selectMemoAll();
			
			app.setTitle("글 개수:" +arr.size());
			//반복문 돌면서 MemoVO를 꺼내고
					//ta에 꺼낸값 출력하기
			app.showTextArea(arr);
		}catch(SQLException e) {
			e.printStackTrace();
			app.showMessage(e.getMessage());
		}
	}//-----------------------------------
	
	public void addMemo() {//insert문
		//[1] app의 tfName, tfMsg에 입력한 값 받아오기
		String name=app.tfName.getText();
		String msg=app.tfMsg.getText();
		
		//[2] 유효성 체크 (null값, 빈문자열 체크)
		if(name==null||name.trim().isEmpty()) {
			app.showMessage("작성자를 입력하세요");
			app.tfName.requestFocus();
			return;
		}
		if(msg==null||msg.trim().isEmpty()) {
			app.showMessage("메모내용을 입력하세요");
			app.tfMsg.requestFocus();
			return;
		}
		
		//[3] [1]번에서 받아온 값을 MemoVO객체에 담아준다
		MemoVO vo=new MemoVO(0,name,msg, null);
		
		//[4] dao의 insertMemo(vo)호출하고 그 결과값을 받아온다
		try {
		int n=dao.insertMemo(vo);
		//[5] 그 결과값에 따른 메시지 처리 
		if(n>0) app.setTitle("글 등록 성공");
		else app.showMessage("글 등록 실패");
		
		}catch(SQLException e) {
			app.showMessage(e.getMessage());
		}
	}

}///////////////////////////////////////////




