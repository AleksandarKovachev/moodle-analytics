function checkLocale() {
	if (!getCookie("locale=")) {
		setLocale();
	}
}

function getLocaleCookie() {
	return getCookie("locale=");
}

function setLocale(locale) {
	var d = new Date();
	d.setTime(d.getTime() + (365 * 24 * 60 * 60 * 1000));
	var expires = "expires=" + d.toUTCString();
	var localeCookie;
	if (locale) {
		localeCookie = "locale=" + locale + ";" + expires + ";path=/"
	} else {
		localeCookie = "locale=" + getBrowserLang() + ";" + expires + ";path=/"
	}
	document.cookie = localeCookie;
	location.reload();
}

function getBrowserLang() {
	if (navigator.languages != undefined)
		return navigator.languages[0];
	else
		return navigator.language;
}

function getCookie(name) {
	var decodedCookie = decodeURIComponent(document.cookie);
	var ca = decodedCookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ') {
			c = c.substring(1);
		}
		if (c.indexOf(name) == 0) {
			return c.substring(name.length, c.length);
		}
	}
	return null;
}