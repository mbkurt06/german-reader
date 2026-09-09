# Launcher artwork

`launcher-source.png` is the single mascot/book illustration attached to the
referenced ChatGPT conversation, recovered as `image.png`. The supplied original
`/mnt/data/a_bright_polished_3d_cgi_app_icon_style_illustrat.png` path was not
available on this machine. The attached artwork is preserved without cropping.

Run `python3 tools/generate_launcher_icons.py` with Pillow 11.3.0 to regenerate.
The foreground is 108dp with a centered 46dp artwork square. Its diagonal is
65.05dp, inside Android's guaranteed 66dp safe circle, preserving every element
under supported launcher masks. The white background matches the source margin.
This deliberately favors full visibility over maximum artwork size.

| Density | Foreground | Legacy / round |
| --- | --- | --- |
| mdpi | 108px | 48px |
| hdpi | 162px | 72px |
| xhdpi | 216px | 96px |
| xxhdpi | 324px | 144px |
| xxxhdpi | 432px | 192px |

Both adaptive XML files and the manifest keep their existing references.
The existing vector book/speech monochrome icon remains for Android themed icons;
the full-color illustration is used when themed icons are disabled.

Safe area reference: https://developer.android.com/codelabs/basic-android-kotlin-compose-training-change-app-icon
