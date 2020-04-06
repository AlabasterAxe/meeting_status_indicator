let setEndpoint = document.getElementById('setEndpoint');
let endpointElement = document.getElementById('endpoint');
let resetTabs = document.getElementById('reset');


setEndpoint.onclick = () => {
  let endpoint = endpointElement.value;
  chrome.storage.local.set({endpoint: endpoint});
};

resetTabs.onclick = () => {
  chrome.storage.local.get(null, (data)=>{
    if (data && data.hangouts) {
      chrome.storage.local.set({hangouts: []});
    }
  })
}

chrome.storage.local.get(null, (data)=>{
  if (data && data.endpoint) {
    endpointElement.value = data.endpoint;
  }
})