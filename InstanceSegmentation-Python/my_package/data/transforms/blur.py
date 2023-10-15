# Imports
import numpy as np
from PIL import Image, ImageFilter


class BlurImage(object):
    '''
        Applies Gaussian Blur on the image.
    '''

    def __init__(self, radius):
        '''
            Arguments:
            radius (int): radius to blur
        '''

        # Write your code here
        self.radius = radius

    def __call__(self, image):
        '''
            Arguments:
            image (numpy array or PIL Image)

            Returns:
            image (numpy array or PIL Image)
        '''
        imageIsArray = False
        if isinstance(image, np.ndarray):
            image = Image.fromarray(image)
            imageIsArray = True

        # Write your code here
        image = image.filter(ImageFilter.GaussianBlur(radius=self.radius))

        if imageIsArray:
            return np.array(image)
        else:
            return image