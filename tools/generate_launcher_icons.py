"""Regenerate launcher resources with Python 3 and Pillow 11.3.0."""
from pathlib import Path
from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parents[1]
RES = ROOT / "app/src/main/res"
SOURCE = Image.open(ROOT / "artwork/launcher-source.png").convert("RGBA")
DENSITIES = {"mdpi": 1, "hdpi": 1.5, "xhdpi": 2, "xxhdpi": 3, "xxxhdpi": 4}


def foreground(scale):
    size = round(108 * scale)
    # The entire 46dp square fits in the guaranteed 66dp diameter circle:
    # 46 * sqrt(2) < 66. No crop, redrawing, or removal of source elements.
    art_size = round(46 * scale)
    layer = Image.new("RGBA", (size, size))
    art = SOURCE.resize((art_size, art_size), Image.Resampling.LANCZOS)
    offset = (size - art_size) // 2
    layer.alpha_composite(art, (offset, offset))
    return layer


def masked(image, rounded):
    mask = Image.new("L", image.size)
    draw = ImageDraw.Draw(mask)
    box = (0, 0, image.width - 1, image.height - 1)
    if rounded:
        draw.ellipse(box, fill=255)
    else:
        draw.rounded_rectangle(box, radius=image.width * 0.22, fill=255)
    image = image.copy()
    image.putalpha(mask)
    return image


if __name__ == "__main__":
    # Legacy images use the adaptive icon's central 72dp viewport.
    layer = foreground(12)
    composite = Image.new("RGBA", layer.size, "white")
    composite.alpha_composite(layer)
    viewport = composite.crop((216, 216, 1080, 1080))
    for density, scale in DENSITIES.items():
        drawable = RES / f"drawable-{density}"
        mipmap = RES / f"mipmap-{density}"
        drawable.mkdir(exist_ok=True)
        mipmap.mkdir(exist_ok=True)
        foreground(scale).save(drawable / "ic_launcher_foreground.png", optimize=True)
        size = round(48 * scale)
        for name, rounded in (("ic_launcher", False), ("ic_launcher_round", True)):
            masked(viewport, rounded).resize((size, size), Image.Resampling.LANCZOS).save(
                mipmap / f"{name}.png", optimize=True
            )
