import requests
import json

def main(path):
    api_endpoint = "https://cvprojectclassification-prediction.cognitiveservices.azure.com/customvision/v3.0/Prediction/da02f51c-d6fd-4cd4-970e-020221a8b5f9/classify/iterations/CV_Final/url"
    api_key = "4a67ebbb02d845ed90c65fde59ccdc6a"

    # Define the image URL you want to classify
    image_url = path

    # Set the request headers
    headers = {
        'Content-Type': 'application/json',
        'Prediction-Key': api_key
    }

    # Set the request body with the image URL
    data = {
        'Url': image_url
    }

    # Make the POST request
    response = requests.post(api_endpoint, headers=headers, data=json.dumps(data))

    # Get the response code
    response_code = response.status_code

    if response_code == 200:
        # Read and process the response
        response_data = response.json()

        # Print the top two predictions
        for i, prediction in enumerate(response_data['predictions'][:1]):
            class_name = prediction['tagName']
            probability = prediction['probability']
            ans = class_name
        return ans
    else:
        # Handle errors
        ans = "Prediction failed with status code: {response_code}"
        return ans