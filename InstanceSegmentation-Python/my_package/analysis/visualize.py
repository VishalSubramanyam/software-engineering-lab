from PIL import Image, ImageDraw
import numpy as np

def plot_visualization(image, predictions, outputFilename, outputFolder):
    # sort the predictions
    sorted(predictions, key= lambda x : x[3])
    for j in range(0, min(len(predictions), 3)):
            image[0][predictions[j][1][0] > 0.4] = 0
            image[1][predictions[j][1][0] > 0.4] = 0
            image[2][predictions[j][1][0] > 0.4] = 1

    image *= 255
    image = np.array(image, dtype=np.uint8)

    currImage = Image.fromarray(image.transpose(1,2,0))
    d = ImageDraw.Draw(currImage)
    for j in range(0, min(len(predictions), 3)):
         # tuple representing the bounding box
        boundingBox = (predictions[j][0][0][0], predictions[j][0][0][1], predictions[j][0][1][0], predictions[j][0][1][1])

        # drawing the bbox on the image
        d.rectangle(boundingBox, fill = None, outline = (255,0,0), width=2)

        # anchor location at the top-left corner of the bbox
        anchorLocation = (boundingBox[0], boundingBox[1])
        d.text(anchorLocation, predictions[j][2])

    currImage.save(f"{outputFolder}/{outputFilename}.jpg")