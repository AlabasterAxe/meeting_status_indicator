let setEndpoint = document.getElementById('setEndpoint');
let endpointElement = document.getElementById('endpoint');

setEndpoint.onclick = () => {
  let endpoint = endpointElement.value;
  chrome.storage.local.set({endpoint: endpoint});
};

chrome.storage.local.get(null, (data)=>{
  if (data && data.endpoint) {
    endpointElement.innerText = data.endpoint;
  }
})