from PIL import Image
import os

TEMPLATES_DIR = "templates"
PALETTES_DIR = "palettes"
OUTPUT_DIR = "generated"

PALETTES = [
  "amethyst", "copper", "diamond", "emerald",
  "gold", "iron", "lapis", "netherite", "quartz", "redstone"
]

def extract_palette(path):
  img = Image.open(path).convert("RGBA")
  return [img.getpixel((x, 0))[:3] for x in range(img.width)]  # 8x1 strip


def brightness(c):
  # perceptual brightness (better than simple average)
  r, g, b = c
  return 0.299*r + 0.587*g + 0.114*b


def build_mapping(template_pixels, palette):
  # get unique non-transparent colors from template
  unique = list({
    (r, g, b)
    for (r, g, b, a) in template_pixels
    if a != 0
  })

  # sort template colors by brightness
  unique_sorted = sorted(unique, key=brightness)

  # sort palette the same way (just in case)
  palette_sorted = sorted(palette, key=brightness)

  mapping = {}

  n = len(unique_sorted)
  m = len(palette_sorted)

  for i, color in enumerate(unique_sorted):
    # map position proportionally into palette
    idx = int(i * (m - 1) / max(1, n - 1))
    mapping[color] = palette_sorted[idx]

  return mapping


def recolor(image_path, palette_name):
  img = Image.open(image_path).convert("RGBA")
  pixels = list(img.getdata())

  palette_path = os.path.join(PALETTES_DIR, palette_name + ".png")
  palette = extract_palette(palette_path)

  mapping = build_mapping(pixels, palette)

  new_pixels = []
  for (r, g, b, a) in pixels:
    if a == 0:
      new_pixels.append((r, g, b, a))
    else:
      new_pixels.append((*mapping[(r, g, b)], a))

  out = Image.new("RGBA", img.size)
  out.putdata(new_pixels)

  os.makedirs(OUTPUT_DIR, exist_ok=True)

  filename = os.path.basename(image_path).replace(
      ".png", f"_{palette_name}.png"
  )
  out.save(os.path.join(OUTPUT_DIR, filename))


def main():
  for file in os.listdir(TEMPLATES_DIR):
    if not file.endswith(".png"):
      continue

    path = os.path.join(TEMPLATES_DIR, file)

    for palette in PALETTES:
      recolor(path, palette)


if __name__ == "__main__":
  main()