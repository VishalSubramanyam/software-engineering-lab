# Imports
from PIL import Image
import numpy as np

class FlipImage(object):
    '''
        Flips the image.
    '''

    def __init__(self, flip_type='horizontal'):
        '''
            Arguments:
            flip_type: 'horizontal' or 'vertical' Default: 'horizontal'
        '''
        self.flip_type = flip_type
        # Write your code here

    def __call__(self, image):
        '''
            Arguments:
            image (numpy array or PIL image)

            Returns:
            image (numpy array or PIL image)
        '''
        imageIsArray = False
        if isinstance(image, np.ndarray):
            image = Image.fromarray(image)
            imageIsArray = True

        if self.flip_type == "horizontal":
            image = image.transpose(Image.FLIP_LEFT_RIGHT)
        else:
            image = image.transpose(Image.FLIP_TOP_BOTTOM)
        
        if imageIsArray:
            return np.array(image)
        else:
            return image
        # Write your code here
