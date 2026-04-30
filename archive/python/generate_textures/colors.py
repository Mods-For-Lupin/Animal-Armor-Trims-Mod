from PIL import Image
import os

def replace_colors(img_path, palette_str):
    # Load the image
    img = Image.open(img_path).convert("RGBA")

    # Load the palette image
    palette_img = Image.open("palettes/"+palette_str+".png").convert("RGBA")
    palette_pixels = palette_img.getdata()

    # Extract the 8 colors from the palette image
    palette = list(set(palette_pixels))
    palette = palette[:8]  # We only need the first 8 unique colors

    # Map each color in the original image to the closest color in the palette
    new_img = Image.new("RGBA", img.size)
    pixels = img.getdata()
    new_pixels = []
    for pixel in pixels:
        if pixel[3] != 0:  # Ignore transparent pixels
            closest_color = min(palette, key=lambda c: sum((a-b)**2 for a, b in zip(pixel[:3], c[:3])))
            new_pixels.append(closest_color)
        else:
            new_pixels.append(pixel)

    # Replace the pixels in the new image
    new_img.putdata(new_pixels)

    # Save the new image
    new_img.save(image_path.replace("templates", "generated").replace(".png", "_" + palette_str + ".png"))


templates = "templates"
palettes = ["amethyst", "copper", "diamond", "emerald", "gold", "iron", "lapis", "netherite", "quartz", "redstone"]
for filename in os.listdir(templates):
    if filename.endswith(".png"):
        image_path = os.path.join(templates, filename)
        for palette_string in palettes:
            replace_colors(image_path, palette_string)