function submitReview() {
  const name = document.getElementById('name').value;
  const rating = document.getElementById('rating').value;
  const review = document.getElementById('review').value;

  if (name === "" || rating === "" || review === "") {
    document.getElementById('response').innerText = "Please fill all fields.";
    return;
  }

  const xhr = new XMLHttpRequest();
  xhr.open("POST", "http://localhost:3000/review", true);
  xhr.setRequestHeader("Content-Type", "application/json");

  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      document.getElementById('response').innerText = "✅ Review submitted successfully!";
    }
  };

  const data = {
    name: name,
    rating: rating,
    review: review
  };

  xhr.send(JSON.stringify(data));
}
