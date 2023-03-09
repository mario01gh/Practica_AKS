def is_prime(n):
    """Comprueba si un número es primo."""
    if n < 2:
        return False
    for i in range(2, int(n ** 0.5) + 1):
        if n % i == 0:
            return False
    return True

def find_closest_prime(n):
    """Encuentra el número primo más cercano a n."""
    i = n
    while True:
        if is_prime(i):
            return i
        i += 1

for i in range(100000000000000, 10000000000000000, 100000000000000):
    closest_prime = find_closest_prime(i)
    print(closest_prime)
