package practiceprj.ptcprjcjk.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import practiceprj.ptcprjcjk.dto.response.PagingRes;

import java.util.List;

@Getter @Setter @ToString
public class PaginationInfo {
	/*
	 * currentPageNo : 현재 페이지 번호
	 * recordCountPerPage : 페이지당 표시될 컨텐츠 갯수
	 * pageSize : 화면 하단에 표시될 페이지 목록 수
	 * totalRecordCount : 전체 컨텐츠 갯수
	 * 
	 * totalPageCount : 총 페이지 갯수
	 * firstPage : 화면 하단에 표시될 페이지 목록의 첫번째 번호
	 * lastPage : 화면 하단에 표시될 페이지 목록의 마지막 번호
	 * firstRecordIndex : MySQL LIMIT의 전달될 첫번째 인자값
	 */
	private int currentPageNo;
	private int recordCountPerPage;
	private int pageSize;
	private int totalRecordCount;
	
	private int totalPageCount;
	private int firstPage;
	private int lastPage;
	private int firstRecordIndex;
	
	private boolean isNextPage;
	private boolean isPreviousPage;
	
	private List<?> list;
	
	
	
	//총 페이지 갯수
	public int getTotalPageCount() {
		totalPageCount = ((totalRecordCount - 1 ) / recordCountPerPage) +1;
		return totalPageCount;
	}
	//화면 하단에 표시될 페이지 목록의 첫번째 번호
	public int getFirstPage() {
		firstPage = ((currentPageNo - 1 ) / pageSize ) * pageSize + 1;
		return firstPage;
	}
	//화면 하단에 표시될 페이지 목록의 마지막 번호
	public int getLastPage() {
		lastPage = (getFirstPage() + pageSize) - 1;
		if(lastPage > getTotalPageCount()) {
			lastPage = totalPageCount;
		}
		return lastPage;
	}
	//Mysql LIMIT의 첫번째 인자로 전달될 값
	public int getFirstRecordIndex() {
		firstRecordIndex = (currentPageNo-1) * recordCountPerPage;
		return firstRecordIndex;
	}
	//이전 페이지 버튼 활성 여부
	public boolean getIsPreviousPage() {
		return getFirstPage() != 1;
	}
	//다음 페이지 버튼 활성 여부
	public boolean getIsNextPage() {
		return (getLastPage() * recordCountPerPage) < totalRecordCount;
	}
	//현재 페이지 번호 반환
	public void setCurrentPageNo(int page) {
		page = (page == 0 ? 1 : page);
		currentPageNo = getTotalPageCount() < page ? getTotalPageCount() : page;
	}

	public PagingRes getPagingRes(){
		return PagingRes.builder()
				.page(getCurrentPageNo())
				.perpage(getRecordCountPerPage())
				.totalpage(getTotalPageCount())
				.totalrecordcount(totalRecordCount)
				.data(list)
				.build();
	}
}
