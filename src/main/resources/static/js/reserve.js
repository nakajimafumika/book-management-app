import * as PROPERTY from "./module/const.js"; // heder,token
import flatpickr from "./module/flatpickr/index.js"; // flatpickrのモジュール
import { Japanese } from './module/flatpickr/l10n/ja.js';// flatpickr日本語ロケール
import { dateFormatters } from './utils/dateUtils.js';// 日付加工

let fp;
let displayStartDate;
let displayEndDate;

const modal = document.getElementById("reserveModal");
const modalReserveButton = document.getElementById("modal_reserve_button");
const display = document.getElementById("selectedDateDisplay");
const managementId = document.getElementById("managementIdReserve");
const messageBox = document.getElementById("reserveCompleteMessage");

// 1.イベントハンドラ（起点）予約モーダルを開く
modal.addEventListener("shown.bs.modal", handleModalOpen, {
  once: true
});

// 2.モーダルが開いたときの処理
function handleModalOpen() {
  modal.setAttribute("aria-hidden", "false");
  initializeCalendar(managementId.value); // カレンダーの初期化関数呼ばれる
}

// 3.カレンダー初期表示関数
function initializeCalendar(managementId) {
  fetch(`${CONTEXT_PATH}/api/reserve-info?managementId=${managementId}`, {
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
      console.log("予約情報:", data);
	  
	  // 予約件数が上限を超えていた場合の制御
	   if (data.overReserveLimit) {
	     modalReserveButton.disabled = true;// 予約ボタンを押せなくする
		 const warning = document.getElementById("reserveWarning");
		  warning.textContent = "予約件数が上限(3件)を超えています。これ以上予約できません。";
		  warning.classList.remove("d-none");  
	     return; // Flatpickr初期化をスキップ
	   }
	   
      const reservedDates = Array.isArray(data.reservedDates) ? data.reservedDates : []; // データから予約期間を取得
      const loanPeriodDates = Array.isArray(data.loanPeriodDates) ? data.loanPeriodDates : []; //　データから貸出期間を取得
      setupCalendar(reservedDates, loanPeriodDates); //　reservedDates, loanPeriodDatesをセットアップカレンダーに渡す
      console.log("Flatpickr初期化完了");
    })
    .catch(error => {
      console.error(error);
      alert(error.message || "カレンダーの初期化に失敗しました");
    });
}

//flatpickr初期化の関数（設定オプションを含む）
function setupCalendar(reservedDates, loanPeriodDates) {
  const tomorrow = new Date(); //予約開始日を明日以降にするため
  tomorrow.setDate(tomorrow.getDate() + 1); // 今日 + 1日
  tomorrow.setHours(0, 0, 0, 0); // 時間を0時にリセット（安全）

  if (fp && typeof fp.destroy === "function") fp.destroy(); //flatpickr を完全に初期化する

  //flatpickrの設定オプション
  fp = flatpickr("#calendarContainer", {
    inline: true, // カレンダーを常に表示-
    locale: "ja",
    mode: "range",
    rangeSeparator: " ～ ",
    minDate: tomorrow, // 選択できる最も早い日付を「明日」に設定
    disable: [...reservedDates, ...loanPeriodDates],

    // 画面に選んだ日付を表示する
    onChange: function(selectedDates, daPROPERTYr, instance) {

      // 予約開始日と終了日を選んだ時の表示
      if (selectedDates.length === 2) {
        displayStartDate = dateFormatters.formatJapaneseDate(
          selectedDates[0]);
        displayEndDate = dateFormatters.formatJapaneseDate(
          selectedDates[1]);

        //選択された日付表示
        display.textContent = `${displayStartDate} ～ ${displayEndDate}`;

        // 日数差を計算
        const diffDays = Math.ceil((selectedDates[1] - selectedDates[0]) /
          (1000 * 60 * 60 * 24));

        // 31日を超えていたら警告＆クリア
        if (diffDays > 31) {
          alert("予約期間は最大1か月（31日）までです。");
          instance.clear();
        }
      } else if (selectedDates.length === 1) {
        // 予約開始日だけの場合の表示
        display.textContent = dateFormatters.formatJapaneseDate(selectedDates[0]);
      }
    }
  }); //flatpickrの設定オプション
}; //flatpickr 初期化の関数

// 日付リセットボタンの処理
document.getElementById("resetReserveDateButton").addEventListener("click", () => {
  fp.clear(); // 選択をクリア  
  display.textContent = "日付を選択してください"; // 表示もクリア
});

// 予約処理
modalReserveButton.addEventListener("click", () => {
  if (!fp || fp.selectedDates.length < 1) {
    alert("日付を選択してください。");
    return; //日付が１日も選択されていない場合処理を止める
  }

  const [reservesDate, returnDueDate = reservesDate] = fp.selectedDates; //同じ日付を選んだらreturnDueDateにreservesDateをいれる

  // 送信用に日付データを加工
  const formattedStart = dateFormatters.formatDate(reservesDate);
  const formattedEnd = dateFormatters.formatDate(returnDueDate);

  console.log("予約対象本ID:", managementId.value);
  console.log("選択日付範囲:", reservesDate, returnDueDate);
  console.log("送信日付範囲:", formattedStart, formattedEnd);

  // 送信データ
  const reserveInfo = {
    managementId: managementId.value,
    reservesDate: formattedStart,
    returnDueDate: formattedEnd
  }

  // 登録
  fetch(`${CONTEXT_PATH}/api/reserve-register`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        [PROPERTY.header]: PROPERTY.token
      },
      body: JSON.stringify(reserveInfo)
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
      console.log("予約成功:", data);
      modalReserveButton.disabled = true;　// 貸出ボタンを一回しか押せないようにする

      // 完了メッセージ表示
      messageBox.classList.remove("d-none");
      messageBox.textContent =
        `予約が完了しました。（期間：${displayStartDate} ～ ${displayEndDate}）`;
    })
	.catch(error => {
	  console.error("エラー:", error);
	  alert(error.message); 
	});

}); // modalReserveButton.addEventListener