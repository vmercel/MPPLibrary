import os

def list_files(output_file="file_list.txt"):
    # Get current working directory
    current_directory = os.getcwd()

    # Open the file in write mode
    with open(output_file, "w") as file:
        # Loop through the items in the current directory
        for filename in os.listdir(current_directory):
            # Check if it's a file (not a directory)
            if os.path.isfile(os.path.join(current_directory, filename)):
                # Write the filename in quotes
                file.write(f'"{filename}"\n')
    
    print(f"List of files saved in '{output_file}'")

# Run the function
list_files()
