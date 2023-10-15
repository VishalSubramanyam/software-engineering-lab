#ifndef ASSIGNMENT2_INCLUDE_NODE_H
#define ASSIGNMENT2_INCLUDE_NODE_H
#include "GeographicLib/Geodesic.hpp"
#include <memory>
#include <string>

using std::shared_ptr;

class node
{
public:
  static GeographicLib::Geodesic earth; ///< Used for calculating geodesic distances with Earth's physical parameters
  long long int id; ///< The value of the ID attribute of the node
  std::string name; ///< name of the node, if available
  GeographicLib::Math::real lat, lon; ///< latitude and longitude
  bool visited = false; ///< used during Dijkstra's algorithm
  double dijkstraDistance = 1.0 / 0.0; ///< setting it to positive infinity, so relaxation can be performed later
  node *parent = nullptr; ///< used to trace the path by moving from node to parent to grandparent and so on
  std::vector<node *> neighbours; ///< used as an adjacency data structure that tells us which nodes are connected to

  node(long long int const &id, const GeographicLib::Math::real &lat, const GeographicLib::Math::real &lon) : id(id),
																											  lat(lat),
																											  lon(lon)
  {
  }

  /**
   * Returns the distance between this node and the other node passed as an argument
   * @param other [in] The other node
   * @return The distance between this and the other node
   */
  GeographicLib::Math::real distance(const node &other) const
  {
	GeographicLib::Math::real distanceBetweenNodes;
	earth.Inverse(this->lat, this->lon, other.lat, other.lon, distanceBetweenNodes);
	return distanceBetweenNodes;
  }
};

#endif //ASSIGNMENT2_INCLUDE_NODE_H
