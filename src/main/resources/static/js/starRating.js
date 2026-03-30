// ページ読み込み時に描画
const score = parseFloat(document.getElementById("starRating").dataset.score);
renderStarRating(score);


// レビューの星表示のための関数
function renderStarRating(score) {
  const container = document.getElementById("starRating");

  const fullStars = Math.floor(score); // ★の数
  const decimal = Math.round((score % 1) * 10); // レビューの小数点第1位
  const halfStar　= (decimal >= 1) ? 1 : 0 ;//半分の星
  const emptyStars = 5 - fullStars - (halfStar); // ☆の数
   
  // ★の数を描画
  for (let i = 0; i < fullStars; i++) {
    container.innerHTML += '<i class="fa-solid fa-star" style="color: #FFD43B;"></i>';
  }
  // 半分の星を描画
  if (halfStar) {
    container.innerHTML += '<i class="fa-solid fa-star-half-stroke" style="color: #FFD43B;"></i>';
  }
  
  // ☆の数を描画
  for (let i = 0; i < emptyStars; i++) {
    container.innerHTML += '<i class="fa-regular fa-star" style="color: #FFD43B;"></i>';
  }
  //　評価を描画
  container.innerHTML += `<span class="ms-2">${score} / 5</span>`;
}



