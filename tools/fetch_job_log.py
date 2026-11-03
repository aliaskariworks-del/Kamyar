"""Fetch a GitHub Actions job log, forcing IPv4 (this network refuses IPv6)."""
import json
import socket
import sys
import urllib.request

_orig = socket.getaddrinfo


def ipv4_only(host, port, family=0, type=0, proto=0, flags=0):
    return _orig(host, port, socket.AF_INET, type, proto, flags)


socket.getaddrinfo = ipv4_only

token = sys.argv[1]
job_id = sys.argv[2]
repo = sys.argv[3]

api = f"https://api.github.com/repos/{repo}/actions/jobs/{job_id}/logs"
req = urllib.request.Request(api, headers={
    "Authorization": f"Bearer {token}",
    "Accept": "application/vnd.github+json",
    "User-Agent": "kamyar-build-check",
})

no_redirect = urllib.request.build_opener(urllib.request.HTTPRedirectHandler)


class NoRedirect(urllib.request.HTTPRedirectHandler):
    def redirect_request(self, *a, **kw):
        return None


opener = urllib.request.build_opener(NoRedirect)
try:
    opener.open(req)
    print("no redirect returned")
except urllib.error.HTTPError as e:
    if e.code not in (301, 302, 303, 307, 308):
        print("HTTP", e.code, e.read()[:300])
        raise SystemExit(1)
    location = e.headers.get("Location")
    print("redirect host:", location.split("/")[2])
    req2 = urllib.request.Request(location, headers={"User-Agent": "kamyar-build-check"})
    body = urllib.request.urlopen(req2, timeout=60).read()
    print("log bytes:", len(body))
    sys.stdout.write(body.decode("utf-8", "replace"))