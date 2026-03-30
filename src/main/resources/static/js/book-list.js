import {TabulatorFull as Tabulator} from "./module/tabulator_esm.min.js";

 let table;

  // 書籍一覧をサーバーから取得してtabulatorで表示させる
  fetch(`${CONTEXT_PATH}/api/book-list`)
	.then(response => {
	  	        if (!response.ok) throw new Error("予約情報の取得に失敗しました");
	  	        return response.json();
	  	      })
		
    .then(data => {
      table = new Tabulator("#book-list", {
        data: data,
        pagination: true,
        paginationSize: 10,
        layout: "fitColumns",
       
		columns: [
		  {title: "管理番号", field: "managementId", headerSort: false },
		  { title: "タイトル", field: "title", headerSort: false },
		  { title: "作者", field: "author", headerSort: false },
		  { title: "状況", field: "status",  headerSort: false },
		  {
		    title: "詳細・貸出/予約",
		    field: "id",
		    hozAlign: "center",
		    headerSort: false,
			
			// 本の詳細画面に遷移するためのリンク
		    formatter: (cell) => {
		      const id = cell.getValue(); // ←このセルの値（field: "id"）を取得
		      return `<a href="${CONTEXT_PATH}/book/detail/${id}" class="btn btn-primary btn-sm">詳細・貸出/予約</a>`;
		    }
		  },
		  
		  //  並べ替え用の非表示列
		  { field: "id", sorter: "number", visible: false },
		  { field: "review", sorter: "number", visible: false }
		]
		});
	  })
	   .catch(error => {
	     console.error("書籍一覧の取得に失敗しました:", error);
	     const container = document.getElementById("book-list");
	     container.innerHTML = `<div class="alert alert-danger">書籍一覧の取得に失敗しました。時間をおいて再度お試しください。</div>`;
		 });

		 //　キーワード検索イベント
		 document.getElementById("searchInput").addEventListener("input", applyFilters);

		 //　貸出状況の絞り込みイベント
		 document.getElementById("statusSelector").addEventListener("change", applyFilters);

		 //　並び替えイベント
		 document.getElementById("sortSelector").addEventListener("change",  (e) => {
		 	  const value = e.target.value;
		 	  if (value === "new") {
		 	    table.setSort("id", "desc");
		 	  } else if (value === "rating") {
		 	    table.setSort("review", "desc");
		 	  } else {
		 	    table.clearSort();
		 	  }
		 	});
		 
	  // キーワード検索と貸出状況を絞り込みするための関数
	  function applyFilters() {
	    const keyword = document.getElementById("searchInput").value.toLowerCase(); // 検索した文字の大文字を小文字に変換
	    const status = document.getElementById("statusSelector").value;

	    table.setFilter((data) => {
	      // 貸出状況フィルター
	      if (status === "貸出中" && data.status !== "貸出中") {
	        return false; // 表示しない
	      }
	      if (status === "除外:貸出中" && data.status === "貸出中") {
	        return false;
	      }

	      // キーワードフィルター（title または author に含まれる）
	      if (keyword) {
	        const titleMatch = data.title && data.title.toLowerCase().includes(keyword); // 書籍の大文字を小文字に変換して検索
	        const authorMatch = data.author && data.author.toLowerCase().includes(keyword); // 作者の大文字を小文字に変換して検索
	     
		  if (!titleMatch && !authorMatch) {
	          return false;
	        }
	      }
	      return true; 
	    });
	  }

