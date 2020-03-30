'use strict';

const HANGOUTS_PREFIX = "https://hangouts.google.com/call/";
const DEFAULT_SIGN_ENDPOINT = "http://192.168.1.22:5000";

function handleRemovedTab(tabId, existingHangouts, signEndpoint) {
  const tabIdx = existingHangouts.indexOf(tabId);
  if (tabIdx >= 0) {
    existingHangouts.splice(tabIdx)
    if (existingHangouts.length === 0) {
      fetch(signEndpoint + "/off?source=vc", {
        mode: "no-cors"
      });
    }
  }
}

  chrome.tabs.onUpdated.addListener((tabId, changeInfo, tab) => {
    if (changeInfo.url) {
      chrome.storage.local.get(null, (value) => {
        const existingHangouts = value ? value.hangouts || [] : [];
        const signEndpoint = value ? value.endpoint || DEFAULT_SIGN_ENDPOINT : DEFAULT_SIGN_ENDPOINT;
        if (changeInfo.url.startsWith(HANGOUTS_PREFIX)) {
          if (existingHangouts.length === 0) {
            fetch(signEndpoint + "/on?source=vc", {
              mode: "no-cors"
            });
          }
          existingHangouts.push(tabId);
        } else {
          handleRemovedTab(tabId, existingHangouts, signEndpoint);
        }
        chrome.storage.local.set({hangouts: Array.from(new Set(existingHangouts))});
      });
    }
  });

  chrome.tabs.onRemoved.addListener((tabId) => {
    chrome.storage.local.get(null, (value) => {
      const existingHangouts = value ? value.hangouts || [] : [];
        const signEndpoint = value ? value.endpoint || DEFAULT_SIGN_ENDPOINT : DEFAULT_SIGN_ENDPOINT;
      handleRemovedTab(tabId, existingHangouts, signEndpoint);
      chrome.storage.local.set({hangouts: existingHangouts});
    });
  });
