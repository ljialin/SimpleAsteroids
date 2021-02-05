import gym

# env = gym.make('CartPole-v0')
# env = gym.make("Pong-v0")
env = gym.make("Alien-v0")
env = gym.make("SpaceInvaders-v4")

from gym import envs

print(envs.registry.all().__sizeof__())

# i = 0
# for e in envs.registry.all():
#     i+=1
#     print("Env {} : \t {}".format(i, e))


for i_episode in range(1):
    observation = env.reset()
    for t in range(1):
        env.render()
        # how would be copy an env?
        print(observation)
        print(type(observation))
        action = env.action_space.sample()
        observation, reward, done, info = env.step(action)
        if done:
            print("Episode {} finished after {} timesteps".format(i_episode + 1, t + 1))
            break
