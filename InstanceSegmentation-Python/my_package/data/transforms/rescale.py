#Imports
from PIL import Image
import numpy as np
class RescaleImage(object):
    '''
        Rescales the image to a given size.
    '''

    def __init__(self, output_size):
        '''
            Arguments:
            output_size (tuple or int): Desired output size. If tuple, output is
            matched to output_size. If int, smaller of image edges is matched
            to output_size keeping aspect ratio the same.
        '''

        # Write your code here
        self.output_size = int(output_size)
    def __call__(self, image):
        '''
            Arguments:
            image (numpy array or PIL image)

            Returns:
            image (numpy array or PIL image)

            Note: You do not need to resize the bounding boxes. ONLY RESIZE THE IMAGE.
        '''
        imageIsArray = False
        if isinstance(image, np.ndarray):
            image = Image.fromarray(image)
            imageIsArray = True

        # Write your code here
        if self.output_size is tuple:
            image = image.resize(self.output_size)
        else:
            width, height = image.size

            # make two cases
            # One case when the width < height, and the other case for width >= height
            # aspect ratio is calculated accordingly and the image is resized
            # to the nearest integer coordinates possible
            if width < height:
                aspectRatio = height/width
                image = image.resize((self.output_size, int(aspectRatio * self.output_size)))
            else:
                aspectRatio = width/height
                image = image.resize((int(aspectRatio * self.output_size), self.output_size))
        if imageIsArray:
            return np.array(image)
        else:
            return image