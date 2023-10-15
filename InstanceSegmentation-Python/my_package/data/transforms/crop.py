# Imports
from PIL import Image
import numpy as np
import random


class CropImage(object):
    '''
        Performs either random cropping or center cropping.
    '''

    def __init__(self, shape, crop_type='center'):
        '''
            Arguments:
            shape: output shape of the crop (h, w)
            crop_type: center crop or random crop. Default: center
        '''
        self.shape = shape
        self.crop_type = crop_type
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

        # Write your code here
        width, height = image.size
        new_width, new_height = self.shape
        if self.crop_type == 'center':
            left = (width - new_width)/2
            right = (width + new_width)/2
            top = (height - new_height)/2
            bottom = (height + new_height)/2
            image =  image.crop((left, top, right, bottom))
        else:
            left = random.randint(0, width - 1)
            right = random.randint(left, width - 1)
            top = random.randint(0, height - 1)
            bottom = random.randint(top, height - 1)
            image = image.crop((left, top, right, bottom))
        if imageIsArray:
            return np.array(image)
        else:
            return image