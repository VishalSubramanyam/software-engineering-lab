#Imports
from msilib.schema import InstallUISequence
from PIL import Image
import numpy as np
class RotateImage(object):
    '''
        Rotates the image about the centre of the image.
    '''

    def __init__(self, degrees):
        '''
            Arguments:
            degrees: rotation degree.
        '''
        # Write your code here
        self.degrees = degrees
    def __call__(self, sample):
        '''
            Arguments:
            image (numpy array or PIL image)

            Returns:
            image (numpy array or PIL image)
        '''
        if isinstance(sample, np.ndarray):
            temp = Image.fromarray(sample)
            return temp.rotate(self.degrees)
        else:
            return sample.rotate(self.degrees)

        # Write your code here