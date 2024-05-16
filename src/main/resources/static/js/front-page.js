const BASE_URL = '';

const loadHTML = async(url, target) => {
  const fullURL = BASE_URL + url;

  try {
    const response = await fetch(fullURL);
    const data = await response.text();
    $('#contentArea').html(data);
  } catch (err) {
    console.error(err);
  }
  
}

document.addEventListener("DOMContentLoaded", async () => {
  await loadHTML('/html/f-products.html', '#contentArea');
});