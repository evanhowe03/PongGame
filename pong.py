import pygame
import sys

# Initialize Pygame
pygame.init()

# Set up the display
width, height = 640, 480
screen = pygame.display.set_mode((width, height))
pygame.display.set_caption('Test Window')

# Set up colorsT
black = (0, 0, 0)
white = (255, 255, 255)

# Set up font
font = pygame.font.Font(None, 36)

# Initial message
message = 'Test'

# Main game loop
running = True
while running:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
        if event.type == pygame.MOUSEBUTTONDOWN:
            # Check if the mouse is clicked within the box
            text_rect = pygame.Rect((width // 2 - 50, height // 2 - 20), (100, 40))  # Box dimensions
            if text_rect.collidepoint(event.pos):
                message = 'Passed'  # Change message on click

    # Fill the background
    screen.fill(black)

    # Render text
    text = font.render(message, True, white)
    text_rect = text.get_rect(center=(width // 2, height // 2))
    screen.blit(text, text_rect)

    # Draw a rectangle around the text
    pygame.draw.rect(screen, white, text_rect, 2)  # 2 pixels thick

    # Update the display
    pygame.display.flip()

# Quit Pygame
pygame.quit()
sys.exit()
