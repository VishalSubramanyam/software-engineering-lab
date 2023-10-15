#Imports
from my_package.model import InstanceSegmentationModel
from my_package.data import Dataset
from my_package.analysis import plot_visualization
from my_package.data.transforms import FlipImage, RescaleImage, BlurImage, CropImage, RotateImage
import numpy as np
import matplotlib.pyplot as plt
from PIL import Image

GENERAL_OUTPUT_FOLDER = "./output"
EXPERIMENT_OUTPUT_FOLDER = "./experimentOutput"

def experiment(annotation_file, segmentor, transforms, outputs):
    '''
        Function to perform the desired experiments

        Arguments:
        annotation_file: Path to annotation file
        segmentor: The image segmentor
        transforms: List of transformation classes
        outputs: path of the output folder to store the images
    '''

    #Create the instance of the dataset.
    datasetObject = Dataset(annotation_file, transforms)

    #Iterate over all data items.
    for i in range(0, len(datasetObject)):
        # perform segmentation on each element in the datasetObject
        segmentation_wrapper(segmentor, datasetObject, i, i, GENERAL_OUTPUT_FOLDER)


    #Do the required analysis experiments.
    
    # roll number: 20CS1008"1"
    imageIndex = 1
    originalImage = Image.open(f"./data/imgs/{imageIndex}.jpg")
    originalImages = []
    # a) Original image experiment
    datasetObject = Dataset(annotation_file, [])
    originalImages.append(datasetObject[imageIndex]["image"])
    segmentation_wrapper(segmentor, datasetObject, imageIndex, 0, EXPERIMENT_OUTPUT_FOLDER)
    
    # b) Horizontally flipped image experiment
    datasetObject = Dataset(annotation_file, [FlipImage("horizontal")])
    originalImages.append(datasetObject[imageIndex]["image"])
    segmentation_wrapper(segmentor, datasetObject, imageIndex, 1, EXPERIMENT_OUTPUT_FOLDER)

    # c) Blurred image experiment
    datasetObject = Dataset(annotation_file, [BlurImage(2)])
    originalImages.append(datasetObject[imageIndex]["image"])
    segmentation_wrapper(segmentor, datasetObject, imageIndex, 2, EXPERIMENT_OUTPUT_FOLDER)
    
    # d) 2x scaled image experiment
    datasetObject = Dataset(annotation_file, [RescaleImage(2 * min(originalImage.width, originalImage.height))])
    originalImages.append(datasetObject[imageIndex]["image"])
    segmentation_wrapper(segmentor, datasetObject, imageIndex, 3, EXPERIMENT_OUTPUT_FOLDER)
    
    # e) 0.5x image experiment
    datasetObject = Dataset(annotation_file, [RescaleImage(0.5 * min(originalImage.width, originalImage.height))])
    originalImages.append(datasetObject[imageIndex]["image"])
    segmentation_wrapper(segmentor, datasetObject, imageIndex, 4, EXPERIMENT_OUTPUT_FOLDER)
    
    # f) 90 degrees clockwise image experiment
    datasetObject = Dataset(annotation_file, [RotateImage(-90)])
    originalImages.append(datasetObject[imageIndex]["image"])
    segmentation_wrapper(segmentor, datasetObject, imageIndex, 5, EXPERIMENT_OUTPUT_FOLDER)

    # g) 45 degrees counterclockwise image experiment
    datasetObject = Dataset(annotation_file, [RotateImage(45)])
    originalImages.append(datasetObject[imageIndex]["image"])
    segmentation_wrapper(segmentor, datasetObject, imageIndex, 6, EXPERIMENT_OUTPUT_FOLDER)

    fig = plt.figure(figsize=(20000,20000))
    ROWS = 7
    COLS = 2
    plt.gcf().set_dpi(1500)
    for i in range(0, 7):
        ax = fig.add_subplot(ROWS, COLS, 2*i + 2)
        ax.set_aspect(5)
        plt.imshow(originalImages[i].transpose(1,2,0), interpolation="nearest")
    
    for i in range(0, 7):
        ax = fig.add_subplot(ROWS, COLS, 2*i + 1)
        ax.set_aspect(5)
        plt.imshow(Image.open(f"{EXPERIMENT_OUTPUT_FOLDER}/{i}.jpg"), interpolation="nearest")

    plt.savefig(f"{EXPERIMENT_OUTPUT_FOLDER}/plot.jpg")
    
# wrapper function for promoting code reuse involving the segmentor model
def segmentation_wrapper(segmentor, datasetObject, dataIndex, outputFilename, outputFolder):
    # use the segmentor to get the predictions
    pred_boxes, pred_masks, pred_class, pred_score = segmentor(datasetObject[dataIndex]["image"])

    # zip all the predictions, so that each bbox can be accessed like a record, along with other attributes
    predictions = list(zip(pred_boxes, pred_masks, pred_class, pred_score))

    currentImage = datasetObject[dataIndex]["image"].copy()
    plot_visualization(currentImage, predictions, outputFilename, outputFolder)




def main():
    segmentor = InstanceSegmentationModel()
    experiment('./data/annotations.jsonl', segmentor, [FlipImage(), BlurImage(2)], None) # Sample arguments to call experiment()

if __name__ == '__main__':
    main()
