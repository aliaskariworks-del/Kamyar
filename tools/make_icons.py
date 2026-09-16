"""Generate the Kamyar launcher icons (no binary assets in the repo)."""
from PIL import Image, ImageDraw
import os

BASE = r"D:\AndroidProjects\Kamyar\app\src\main\res"
DENSITIES = {"mdpi": 48, "hdpi": 72, "xhdpi": 96, "xxhdpi": 144, "xxxhdpi": 192}
FG = {"mdpi": 108, "hdpi": 162, "xhdpi": 216, "xxhdpi": 324, "xxxhdpi": 432}
BG = (0x6C, 0x4A, 0xB6, 255)


def draw_note(size: int, with_bg: bool) -> Image.Image:
    img = Image.new("RGBA", (size, size), BG if with_bg else (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    s = size / 108.0  # adaptive-icon safe zone is 108dp

    white = (255, 255, 255, 255)
    # note head (ellipse) bottom-left
    hx, hy, hr = 34 * s, 74 * s, 15 * s
    d.ellipse([hx - hr, hy - hr * 0.78, hx + hr, hy + hr * 0.78], fill=white)
    # stem
    sx = hx + hr * 0.92
    d.rectangle([sx, 26 * s, sx + 6.5 * s, hy], fill=white)
    # flag
    d.polygon(
        [
            (sx + 6.5 * s, 26 * s),
            (sx + 30 * s, 34 * s),
            (sx + 30 * s, 47 * s),
            (sx + 6.5 * s, 39 * s),
        ],
        fill=white,
    )
    # small second note head for a nicer mark
    h2x, h2y, h2r = 70 * s, 62 * s, 11 * s
    d.ellipse([h2x - h2r, h2y - h2r * 0.78, h2x + h2r, h2y + h2r * 0.78], fill=white)
    d.rectangle([h2x + h2r * 0.78, 22 * s, h2x + h2r * 0.78 + 5 * s, h2y], fill=white)
    d.polygon(
        [
            (h2x + h2r * 0.78 + 5 * s, 22 * s),
            (h2x + 34 * s, 29 * s),
            (h2x + 34 * s, 40 * s),
            (h2x + h2r * 0.78 + 5 * s, 33 * s),
        ],
        fill=white,
    )
    return img


def rounded(img: Image.Image, radius_ratio: float = 0.22) -> Image.Image:
    mask = Image.new("L", img.size, 0)
    ImageDraw.Draw(mask).rounded_rectangle(
        [0, 0, img.size[0] - 1, img.size[1] - 1],
        radius=int(img.size[0] * radius_ratio),
        fill=255,
    )
    out = Image.new("RGBA", img.size, (0, 0, 0, 0))
    out.paste(img, (0, 0), mask)
    return out


created = []
for density, px in DENSITIES.items():
    folder = os.path.join(BASE, f"mipmap-{density}")
    os.makedirs(folder, exist_ok=True)

    legacy = rounded(draw_note(px, with_bg=True))
    legacy = legacy.resize((px, px), Image.LANCZOS)
    p1 = os.path.join(folder, "ic_launcher.png")
    legacy.save(p1)
    p2 = os.path.join(folder, "ic_launcher_round.png")
    legacy.save(p2)

    fpx = FG[density]
    fg = draw_note(fpx, with_bg=False)
    p3 = os.path.join(folder, "ic_launcher_foreground.png")
    fg.save(p3)
    created += [p1, p2, p3]

for p in created:
    print(os.path.relpath(p, BASE), os.path.getsize(p), "bytes")
print("total files:", len(created))
