import * as PROPERTY from "./module/const.js"; // heder,token
import flatpickr from "./module/flatpickr/index.js"; // flatpickrのモジュール
import { Japanese } from './module/flatpickr/l10n/ja.js';// flatpickr日本語ロケール
import { dateFormatters } from './utils/dateUtils.js';// 日付加工

let fp; // flatpickrの設定
let loanDate; // 貸出開始日
let selectedEndDate; //　返却日

const loanModal = document.getElementById("loanModal"); // 貸出モーダル
const modalLoanButton = document.getElementById("modal_loan_button"); // 貸出処理で使用
const managementId = document.getElementById("managementIdLoan"); // 書籍の管理番号
const display = document.getElementById("selectedDateDisplayLoan"); // 選択した日付を表示する場所
const messageBox = document.getElementById("loanSuccessMessage"); // 登録完了を表示する場所

// 1.イベントハンドラ（起点）貸出モーダルを開く
loanModal.addEventListener("shown.bs.modal", handleModalOpen, {
  once: true
});

// 2.モーダルが開いたときの処理
function handleModalOpen() {
  loanModal.setAttribute("aria-hidden", "false");
  initializeCalendar(managementId.value); // カレンダーの初期化関数呼ばれる
}

// 3.カレンダー初期表示関数
function initializeCalendar(managementId) {
  fetch(`${CONTEXT_PATH}/api/loan-info?managementId=${managementId}`, {
      method: "GET",
      headers: {
        [PROPERTY.header]: PROPERTY.token
      }
    })
    .then(response => {
      if (!response.ok) throw new Error("予約情報の取得に失敗しました");
      return response.json();
    })
    .then(data => {
      console.log("データ取得成功", data);
	  
	  //　予約件数が上限を超えていた場合の制御
	  	   if (data.overLoanLimit) {
	  	     modalLoanButton.disabled = true;// 貸出ボタンを押せなくする
	  		 const warning = document.getElementById("loanWarning");
	  		  warning.textContent = "貸出件数が上限(5件)を超えています。これ以上貸出できません。";
	  		  warning.classList.remove("d-none");  
	  	     return; // Flatpickr初期化をスキップ
	  	   }
		   
      const loginuserReservedDates = data.loginuserReservedDates; // データからログインユーザの予約期間を取得
      const otherReservedDates = data.otherReservedDates; // データからログインユーザ以外の予約期間を取得
      setupCalendar(loginuserReservedDates, otherReservedDates) //　loginuserReservedDates, otherReservedDatesをセットアップカレンダーに渡す
      console.log("Flatpickr初期化完了");
    })
	    .catch(error => {
	      console.error(error);
	      alert(error.message || "カレンダーの初期化に失敗しました");
	    });
	}

//flatpickr初期化の関数（設定オプションを含む）
function setupCalendar(loginuserReservedDates, otherReservedDates) {
  if (fp && typeof fp.destroy === "function") fp.destroy();

  //貸出期間の上限（1か月）の計算
  loanDate = new Date();
  const oneMonthLater = new Date(loanDate);
  oneMonthLater.setMonth(loanDate.getMonth() + 1);

  // flatpickrの設定オプション
  fp = flatpickr("#calendarContainerLoan", {
    inline: true,
    locale:"ja",
    mode: "range",
    rangeSeparator: " ～ ",
    defaultDate: loanDate,
    minDate: loanDate,
    maxDate: oneMonthLater,
    disable: otherReservedDates,

    onChange: function(selectedDates, dateStr, instance) {

      // 終了日だけ取得（開始日は loanDate に固定）
      const endDate = selectedDates[1];
      selectedEndDate = endDate; //  ここで終了日を保持する
      instance.setDate([loanDate, endDate], false); // 開始日を固定＆falseで再発火防止

      //　表示用
      const loanDateStr = dateFormatters.formatJapaneseDate(loanDate);
      const endDateStr = dateFormatters.formatJapaneseDate(endDate);
      display.textContent = `${loanDateStr} ～ ${endDateStr}`;
      console.log("表示する日付:", `${loanDateStr} ～ ${endDateStr}`);
    },

    // ログインユーザーが予約済みならカレンダーに黄色の線を表示
    onDayCreate: function(dObj, dStr, fp, dayElem) {
      const date = dayElem.dateObj.toISOString().split("T")[0]; // Dateを文字列に変換して、形式を揃える
      const isReserved = loginuserReservedDates.includes(date);
      if (isReserved) {
        dayElem.classList.add("user-reserved");
      }
    }
  }); // flatpickrの設定オプションここまで

  //自分の予約日がある場合は凡例を表示
  const legendContainer = document.getElementById("calendarLegend");
  if (loginuserReservedDates.length > 0) {
    legendContainer.innerHTML += `<p><span class="legend-box yellow"></span> あなたの予約日</p>`;
  }
} // function setupCalendar

// 日付リセットボタンの処理
document.getElementById("resetLoanDateButton").addEventListener("click", () => {
  fp.clear(); // 選択をクリア
  fp.setDate([loanDate]); // 開始日だけ再設定
  display.textContent = "日付を選択してください"; // 表示をクリア
});

// 貸出処理
modalLoanButton.addEventListener("click", () => {

  // 送信用に日付データを加工
  const loanStartDate = dateFormatters.formatDate(loanDate);
  const loanEndDate = dateFormatters.formatDate(selectedEndDate);

  // 返却予定日が選択されていなかったら登録できない
  if (!selectedEndDate) {
    alert("必要な情報が不足しています");
    return;
  }

  // 送信データ
  const loanInfo = {
    managementId: managementId.value,
    loanDate: loanStartDate,
    returnDueDate: loanEndDate,
  }

  // 登録
  fetch(`${CONTEXT_PATH}/api/loan-register`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        [PROPERTY.header]: PROPERTY.token
      },
      body: JSON.stringify(loanInfo)
    })
	.then(response => {
	    if (!response.ok) {
	      return response.json().then(errorJson => {
	        throw errorJson;
	      });
	    }
	    return response.text();
	  })
    .then(data => {
      console.log("登録成功:", data);
      modalLoanButton.disabled = true; // 貸出ボタンを一回しか押せないようにする

      // 完了メッセージ表示
      const displayStartDate = dateFormatters.formatJapaneseDate(loanDate);
      const displayEndDate = dateFormatters.formatJapaneseDate(selectedEndDate);
      messageBox.classList.remove("d-none");
      messageBox.textContent =`貸出が完了しました。（期間：${displayStartDate} ～ ${displayEndDate}）`;
    })
    .catch(error => {
      console.error("エラー:", error);
      alert(error.message);
    });
}); // 貸出処理ここまで
