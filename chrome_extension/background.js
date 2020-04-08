'use strict';

const HANGOUTS_PATTERN = 'https://hangouts.google.com/call/*';
const DEFAULT_SIGN_ENDPOINT = 'http://192.168.1.22:5000';

function checkTabsAndUpdateStatusIfNecessary() {
  chrome.storage.local.get(null, value => {
    const committedStatus = value && value.committedStatus === 'boolean' ? value.committedStatus : null;
    const signEndpoint = value && value.endpoint ? value.endpoint : DEFAULT_SIGN_ENDPOINT;

    chrome.tabs.query({ url: HANGOUTS_PATTERN }, tabs => {
      const currentStatus = tabs.length > 0;
      if (currentStatus != committedStatus) {
        const path = currentStatus ? 'on' : 'off';
        fetch(`${signEndpoint}/${path}?source=vc`, {
          mode: 'no-cors',
        }).then(() => {
          chrome.storage.local.set({ committedStatus: currentStatus });
        });
      }
    });
  });
}

chrome.tabs.onUpdated.addListener(checkTabsAndUpdateStatusIfNecessary);

chrome.tabs.onRemoved.addListener(checkTabsAndUpdateStatusIfNecessary);
