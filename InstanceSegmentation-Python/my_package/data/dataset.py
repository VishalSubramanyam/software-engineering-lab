#Imports
from PIL import Image
import numpy as np
import json

class Dataset(object):
    '''
        A class for the dataset that will return data items as per the given index
    '''

    def __init__(self, annotation_file, transforms = None):
        '''
            Arguments:
            annotation_file: path to the annotation file
            transforms: list of transforms (class instances)
                        For instance, [<class 'RandomCrop'>, <class 'Rotate'>]
        '''
        self.annotation_file = annotation_file
        self.transforms = transforms
        self.dataset = []
        
        with open(self.annotation_file, "rt") as annFile:
            lines = annFile.readlines()
            for line in lines:
                parsedJsonLine = json.loads(line)
                self.dataset.append(parsedJsonLine)

    def __len__(self):
        '''
            return the number of data points in the dataset
        '''
        return len(self.dataset)
        

    def __getitem__(self, idx):
        '''
            return the dataset element for the index: "idx"
            Arguments:
                idx: index of the data element.

            Returns: A dictionary with:
                image: image (in the form of a numpy array) (shape: (3, H, W))
                gt_png_ann: the segmentation annotation image (in the form of a numpy array) (shape: (1, H, W))
                gt_bboxes: N X 5 array where N is the number of bounding boxes, each 
                            consisting of [class, x1, y1, x2, y2]
                            x1 and x2 lie between 0 and width of the image,
                            y1 and y2 lie between 0 and height of the image.

            You need to do the following, 
            1. Extract the correct annotation using the idx provided.
            2. Read the image, png segmentation and convert it into a numpy array (wont be necessary
                with some libraries). The shape of the arrays would be (3, H, W) and (1, H, W), respectively.
            3. Scale the values in the arrays to be with [0, 1].
            4. Perform the desired transformations on the image.
            5. Return the dictionary of the transformed image and annotations as specified.
        '''
        chosenAnnotation = self.dataset[idx]
        imagePath = "./data/" + chosenAnnotation["img_fn"]
        pngSegPath = "./data/" + chosenAnnotation["png_ann_fn"]

        image = Image.open(imagePath)
        pngSeg = Image.open(pngSegPath)
        
        imageArray = np.array(image)
        pngSegArray = np.array(pngSeg)

        for transform in self.transforms:
            imageArray = transform(imageArray)
        imageArray = np.array(imageArray, dtype=np.float64)
        pngSegArray = np.array(pngSegArray,  dtype=np.float64)
        imageArray /= 255
        pngSegArray /= 255        
        
        return {
            "image" : imageArray.transpose(2,0,1),
            "gt_png_ann" : pngSegArray,
            "gt_bboxes" :  [[bbox["category"], bbox["bbox"][0], bbox["bbox"][1], bbox["bbox"][0] + bbox["bbox"][2], bbox["bbox"][1] + bbox["bbox"][3]] for bbox in chosenAnnotation["bboxes"]]
        }
        
        